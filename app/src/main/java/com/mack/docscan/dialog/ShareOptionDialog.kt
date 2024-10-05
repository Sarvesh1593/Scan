package com.mack.docscan.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.mack.docscan.R

class ShareOptionDialog(private val listener : ShareDialogListener) : DialogFragment() {
    interface ShareDialogListener {
        fun onSinglePageShare()
        fun onEntireDocumentShare()
    }

    @SuppressLint("InflateParams", "MissingInflatedId")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflator = activity?.layoutInflater
        val view : View = inflator!!.inflate(R.layout.sharedialogoption, null)

        val singlePageButton = view.findViewById<View>(R.id.share_single_page)
        val entireDocumentButton = view.findViewById<View>(R.id.share_entire_document)


        singlePageButton.setOnClickListener{
            listener.onSinglePageShare()
            dismiss()
        }

        entireDocumentButton.setOnClickListener {
            listener.onEntireDocumentShare()
            dismiss()
        }
        return activity?.let {
            AlertDialog.Builder(it)
                .setView(view)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}