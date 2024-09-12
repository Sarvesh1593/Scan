package com.mack.docscan.ViewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _pdfUri = MutableLiveData<Uri>()
    val pdfUri : LiveData<Uri> get() = _pdfUri
    fun setPdfUri(uri : Uri){
        _pdfUri.value = uri
    }
}