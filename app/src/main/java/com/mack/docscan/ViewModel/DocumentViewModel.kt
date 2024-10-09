package com.mack.docscan.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.mack.docscan.database.documentdatabase.Document
import com.mack.docscan.database.documentdatabase.DocumentDao
import com.mack.docscan.database.documentdatabase.DocumentDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DocumentViewModel(application : Application) : AndroidViewModel(application) {
    private val documentDao : DocumentDao = DocumentDatabase.getInstance(application).documentDao()
    val allDocuments : LiveData<List<Document>> = documentDao.getAllDocuments()

    fun insertDocument(document: Document) {
        viewModelScope.launch(Dispatchers.IO) {
            documentDao.insert(document)
        }
    }
    fun deleteDocument(document: Document){
        viewModelScope.launch(Dispatchers.IO){
            documentDao.delete(document)
        }
    }

    fun searchDocuments(text : String) : LiveData<List<Document>> {
        return documentDao.searchDocuments(text)
    }
}