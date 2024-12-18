package com.mack.docscan.ui.mainScreen

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.mack.docscan.Adapter.DocumentPagerAdapter
import com.mack.docscan.R
import com.mack.docscan.ViewModel.ImageSharedViewModel
import com.mack.docscan.databinding.FragmentEditBinding
import com.mack.docscan.dialog.ShareOptionDialog
import com.mack.docscan.bottom_dialog.SharePDFBottomDialog
import com.mack.docscan.database.documentdatabase.Document
import com.mack.docscan.database.documentdatabase.DocumentDatabase
import com.mack.docscan.database.pagedatabase.Page
import com.mack.docscan.database.pagedatabase.PageDatabase
import com.mack.docscan.utils.ImageUtils
import com.mack.docscan.utils.PDFWriterUtil
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class EditFragment : Fragment(), ShareOptionDialog.ShareDialogListener {

    private var binding : FragmentEditBinding? = null
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: DocumentPagerAdapter
    private lateinit var imageSharedViewModel: ImageSharedViewModel
    private lateinit var currentImageUri : Uri
    private var recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditBinding.inflate(layoutInflater,container,false)
        viewPager = binding!!.imageViewPager

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            showExitConfirmationDialog()
        }
        binding?.editBackBtn?.setOnClickListener {
            imageSharedViewModel.resetIndex()
            imageSharedViewModel.clearImageUris()
            findNavController().navigate(EditFragmentDirections.actionEditFragmentToCameraFragment())
        }
        binding?.btnEdit?.setOnClickListener {
            imageSharedViewModel.setImageUri(currentImageUri)
            val currentPage = viewPager.currentItem
            imageSharedViewModel.setCurrentIndex(currentPage)
            findNavController().navigate(EditFragmentDirections.actionEditFragmentToEditPageFragment())
        }
        // This button do for retaking the photo and replacing the existing photo

        return binding?.root
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Exit Confirmation")
            .setMessage("Do you want to delete the images and exit or save them?")
            .setPositiveButton("Save and Exit") { _, _ ->
                // Handle delete and exit
                saveDocument()
                activity?.finish() // Exit the app or navigate to another activity/fragment
            }
            .setNegativeButton("Delete and Exit") { _, _ ->
                imageSharedViewModel.clearImageUris()
                activity?.finish() // Exit the app or navigate to another activity/fragment
            }
            .setNeutralButton("Cancel", null) // Do nothing on cancel
            .show()
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageSharedViewModel =
            ViewModelProvider(requireActivity())[ImageSharedViewModel::class.java]

        setUpPager()

        imageSharedViewModel.imageUris.observe(viewLifecycleOwner) { uri ->
            adapter.updateData(uri)
        }

        imageSharedViewModel.currentIndex.observe(viewLifecycleOwner) { index ->
            viewPager.currentItem = index
        }
        imageSharedViewModel.imageUris.observe(viewLifecycleOwner){ uriList ->
            adapter.updateData(uriList)
            adapter.notifyItemChanged(imageSharedViewModel.currentIndex.value?:0)
        }
        binding?.btnRetake?.setOnClickListener {
            Log.d("retake", it.toString())
            val currentPage = viewPager.currentItem
            imageSharedViewModel.setCurrentIndex(currentPage)
            findNavController().navigate(EditFragmentDirections.actionEditFragmentToCameraFragment())
        }
        binding?.btnScanMore?.setOnClickListener {
            Log.d("ScanMore", it.toString())
            imageSharedViewModel.setCurrentIndex(-1)
            findNavController().navigate(EditFragmentDirections.actionEditFragmentToCameraFragment())
        }
        binding?.OCR?.setOnClickListener {
            val bitmapImage = ImageUtils.loadImageFromUri(requireContext(), currentImageUri)
            processOCRImage(
                bitmapImage,
                onSuccess = { scannedText ->
                    Log.d("Text Recognition", scannedText)
                    imageSharedViewModel.setRecognizedText(scannedText)
                    findNavController().navigate(EditFragmentDirections.actionEditFragmentToOCRFragment())
                },
                onFailure = { exception ->
                    Log.d("TextRecognition", "Error recognizing text", exception)
                })
        }
        binding?.shareButton?.setOnClickListener {
            showShareOptionDialog()
        }
        binding?.btnSave?.setOnClickListener {
            saveDocument()
        }
    }

    private fun showShareOptionDialog() {
         val dialog = ShareOptionDialog(this)
        dialog.show(parentFragmentManager,"ShareOptionDialog")
    }

    // find the ocr of the image
    private fun processOCRImage(bitmap: Bitmap, onSuccess : (String)-> Unit, onFailure: (Exception) -> Unit ){
        val image = InputImage.fromBitmap(bitmap,0)
        recognizer.process(image)
            .addOnSuccessListener { result ->
                val scannedText = result.text
                onSuccess(scannedText)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }

    }

    private fun setUpPager(){
        adapter = DocumentPagerAdapter(imageSharedViewModel.imageUris.value ?: mutableListOf())
        viewPager.adapter = adapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                imageSharedViewModel.setCurrentIndex(position)
                currentImageUri = adapter.getImageUri(position)
            }
        })
    }


    override fun onSinglePageShare() {


       val shareIntent = Intent(Intent.ACTION_SEND).apply {
           putExtra(Intent.EXTRA_STREAM,currentImageUri)
           addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
           type = "image/png"
       }

        startActivity(Intent.createChooser(shareIntent,"Share Image via"))
    }

    override fun onEntireDocumentShare() {
        val imageUriList = imageSharedViewModel.imageUris.value ?: emptyList()

        val pdfWriterUtil = PDFWriterUtil()

        val directoryPath = requireContext().getExternalFilesDir(null)?.path + "/PDFs"
        val pdfFile = File(directoryPath,"Scanned_image.pdf")

        if(!pdfFile.parentFile?.exists()!!){
            pdfFile.parentFile?.mkdirs()
        }

        pdfWriterUtil.generatePdfFromImages(requireContext(),imageUriList)


        val sharePDFBottomDialog  = SharePDFBottomDialog(requireContext(),pdfFile)
        sharePDFBottomDialog.show(parentFragmentManager,"Share PDF Dialog")

    }

    private fun saveDocument(){
        val imageUriList = imageSharedViewModel.imageUris.value ?: return
        val documentName = "DocScan" + SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(
            Date()
        ).toString()

        val document = Document(
            name = documentName,
            imagePath = currentImageUri.toString(),
            pageCount = imageUriList.size,
        )

        lifecycleScope.launch {
            val documentId = DocumentDatabase.getInstance(requireContext()).documentDao().insert(document)

            imageUriList.forEach { uri ->
                val pagePath = uri.toString()
                val documentname = "DocScan" + SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(
                    Date()
                ).toString()

                val page = Page(documentId = documentId, path = pagePath, documentName = documentname)

                PageDatabase.getInstance(requireContext()).pageDao().insert(page)
            }
        }
        findNavController().navigate(EditFragmentDirections.actionEditFragmentToMainScreen())
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        Glide.get(requireActivity()).clearMemory()
    }

    override fun onResume() {
        super.onResume()
        if (imageSharedViewModel.currentIndex.value != -1) {
            imageSharedViewModel.resetIndex()
        }
    }
}


