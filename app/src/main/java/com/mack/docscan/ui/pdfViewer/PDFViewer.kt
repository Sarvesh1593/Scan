package com.mack.docscan.ui.pdfViewer

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.github.barteksc.pdfviewer.PDFView
import com.mack.docscan.ViewModel.SharedViewModel
import com.mack.docscan.databinding.FragmentPDFViewerBinding


class PDFViewer : Fragment() {

    private var _biding : FragmentPDFViewerBinding? = null
    private lateinit var pdfViewPager : ViewPager2
    private lateinit var viewModel : SharedViewModel
    private lateinit var pdfView  : PDFView
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _biding = FragmentPDFViewerBinding.inflate(layoutInflater,container,false)
        pdfView = _biding!!.pdfViewer
        return _biding!!.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        viewModel.pdfUri.observe(viewLifecycleOwner) { uri ->
            uri?.let {
                Log.d("SharedViewModel","This is ${it}")
                displayPdf(it)
            }
        }
        arguments?.getString("pdf_uri")?.let {
            val uri = Uri.parse(it)
            loadPdf(uri)
        }
    }
    @SuppressLint("Recycle")
    private fun displayPdf(uri: Uri){
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        inputStream.let {
            pdfView.fromStream(it).load()
        }
    }
    private fun loadPdf(uri : Uri){
        pdfView.fromUri(uri)
            .defaultPage(0)
            .enableSwipe(true)
            .swipeHorizontal(true)
            .enableDoubletap(true)
            .fitEachPage(true)
            .load()
    }
}