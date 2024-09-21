package com.mack.docscan.ViewModel


import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ImageSharedViewModel : ViewModel() {
    private val _imageUris = MutableLiveData<MutableList<Uri>>(mutableListOf())
    val imageUris: LiveData<MutableList<Uri>> get() = _imageUris

    private val _currentIndex = MutableLiveData<Int>(0)
    val currentIndex: LiveData<Int> get() = _currentIndex

    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: LiveData<Uri?> get() = _imageUri

    // MutableLiveData to hold the recognized text
    private val _recognizedText = MutableLiveData<String>()

    // Public LiveData to expose the recognized text to observers
    val recognizedText: LiveData<String>
        get() = _recognizedText

    fun addImage(uri: Uri) {
        _imageUris.value?.add(uri)
        _imageUris.postValue(_imageUris.value)
    }
    fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
    }


    // Method to update the recognized text
    fun setRecognizedText(text: String) {
        _recognizedText.value = text
    }
    fun replaceImage(uri: Uri, index: Int) {
        val list = _imageUris.value ?: mutableListOf()
        if (index in list.indices) {
            list[index] = uri
            _imageUris.postValue(list) // Post value to notify observers
        } else {
            // Log error and consider adding the image instead if the list is empty
            Log.e("ImageSharedViewModel", "Invalid index: $index for replacement. List size: ${list.size}")
            if (list.isEmpty() && index == 0) {
                list.add(uri)
                _imageUris.postValue(list)
            }
        }
    }


    fun removeImage(index: Int) {
        _imageUris.value?.removeAt(index)
        _imageUris.value = _imageUris.value
    }

    fun setCurrentIndex(index: Int) {
        _currentIndex.value = index
    }
    fun resetIndex() {
        _currentIndex.value = 0
    }

    fun clearImageUris() {
        _imageUris.value?.clear()
        _imageUris.postValue(_imageUris.value)
        resetIndex()
    }
}
