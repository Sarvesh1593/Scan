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

    private val _recognizedText = MutableLiveData<String>()
    val recognizedText: LiveData<String> get() = _recognizedText

    // Add a new image URI
    fun addImage(uri: Uri) {
        _imageUris.value?.let {
            it.add(uri)
            _imageUris.postValue(it) // Post value to notify observers
        }
    }

    // Replace image URI at a specific index
    fun replaceImage(uri: Uri, index: Int) {
        _imageUris.value?.let { list ->
            if (index in list.indices) {
                list[index] = uri
                _imageUris.postValue(list)
            } else {
                Log.e("ImageSharedViewModel", "Invalid index: $index. List size: ${list.size}")
                if (list.isEmpty() && index == 0) {
                    list.add(uri)
                    _imageUris.postValue(list)
                }
            }
        }
    }

    // Remove image at the given index
    fun removeImage(index: Int) {
        _imageUris.value?.let {
            if (index in it.indices) {
                it.removeAt(index)
                _imageUris.postValue(it)
            }
        }
    }

    // Update image at a given index
    fun updateImageAtIndex(newUri: Uri, index: Int) {
        _imageUris.value?.let {
            if (index in it.indices) {
                it[index] = newUri
                _imageUris.postValue(it)
            }
        }
    }

    // Set the current index
    fun setCurrentIndex(index: Int) {
        _currentIndex.value = index
    }

    // Reset the current index to zero
    fun resetIndex() {
        _currentIndex.value = 0
    }

    // Clear all image URIs
    fun clearImageUris() {
        _imageUris.value?.clear()
        _imageUris.postValue(_imageUris.value)
        resetIndex()
    }

    // Set the image URI
    fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
    }

    // Set recognized text
    fun setRecognizedText(text: String) {
        _recognizedText.value = text
    }
}
