package com.mack.docscan.dialog

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mack.docscan.ViewModel.ImageSharedViewModel
import com.mack.docscan.databinding.SharedpdfbottomdialogBinding
import java.io.File

class SharePDFBottomDialog(
    private val context: Context,
    private val pdfFile : File
) : BottomSheetDialogFragment(){
    private var binding : SharedpdfbottomdialogBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SharedpdfbottomdialogBinding.inflate(layoutInflater,container,false)

        binding?.cancelSharePdf?.setOnClickListener {
            dismiss()
        }

        binding?.sharePdf?.setOnClickListener {
            sharePdf(pdfFile)
        }
        return binding!!.root

    }

    private fun sharePdf(pdfFile: File) {
        // Get the content URI using FileProvider
        val uri: Uri = FileProvider.getUriForFile(
            requireContext(), // Use requireContext() if this is a Fragment
            "${requireContext().packageName}.provider", // Make sure this matches the authority in AndroidManifest
            pdfFile
        )

        // Create the share intent
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_STREAM, uri) // Add the content URI from FileProvider
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Grant permission to read the URI
            type = "application/pdf" // Specify the MIME type
        }

        // Start the sharing intent
        startActivity(Intent.createChooser(shareIntent, "Share PDF via"))
    }

}