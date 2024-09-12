package com.mack.docscan.bottom_dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mack.docscan.R
import com.mack.docscan.ViewModel.SharedViewModel
import com.mack.docscan.databinding.BottmSheetDialogUploadBinding

class UploadFileDialog : BottomSheetDialogFragment() {

    private var binding: BottmSheetDialogUploadBinding? = null
    private val _binding get() = binding!!
    private lateinit var viewModel: SharedViewModel

    private lateinit var filePickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryPickerLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottmSheetDialogUploadBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        return _binding.root
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.bg_color)

        filePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    Log.d("UploadFileDialog", "Setting up URI in Upload is $uri")
                    viewModel.setPdfUri(uri)
                    dismiss()
                }
            }
        }

        galleryPickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val singleImage: Uri? = data?.data
                if (data != null) {
                    if (data.clipData != null) {
                        val count = data.clipData!!.itemCount
                        for (i in 0 until count) {
                            val imageUri = data.clipData!!.getItemAt(i).uri
                            // Handle the image URI
                        }
                    }
                } else if (singleImage != null) {
                    val imageUri = singleImage
                    // Handle the single image URI
                }
            }
        }

        _binding.cameraBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mainScreen_to_cameraFragment)
        }

        _binding.galleryBtn.setOnClickListener {
            openGallery()
        }

        _binding.importFileBtn.setOnClickListener {
            openFile()
        }

        _binding.CancelBtn.setOnClickListener {
            dismiss()
        }
    }

    private fun openFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
        }
        filePickerLauncher.launch(intent)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        galleryPickerLauncher.launch(intent)
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
