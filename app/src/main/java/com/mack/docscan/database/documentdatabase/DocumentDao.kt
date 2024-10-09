package com.mack.docscan.database.documentdatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface DocumentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(document: Document) : Long

    @Delete
    suspend fun delete(document: Document)

    @Query("Select * FROM documents ORDER BY documentId DESC")
    fun getAllDocuments() : LiveData<List<Document>>

    @Query("SELECT * FROM documents WHERE name LIKE :text ORDER BY documentId DESC")
    fun searchDocuments(text : String) : LiveData<List<Document>>
}