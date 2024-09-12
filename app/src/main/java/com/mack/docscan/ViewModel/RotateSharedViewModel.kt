package com.mack.docscan.ViewModel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RotateSharedViewModel : ViewModel() {
    private val _imageUri = MutableLiveData<Uri>()
    val uri: LiveData<Uri> get() = _imageUri


    fun setUri(uri : Uri) {
        _imageUri.value = uri
    }

    fun setImageUri(uri : Uri){
        _imageUri.value =  uri
    }

}