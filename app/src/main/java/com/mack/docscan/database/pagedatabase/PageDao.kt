package com.mack.docscan.database.pagedatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface PageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(page: Page)

    @Query("SELECT * FROM pages WHERE documentId = :documentId ORDER BY pageId ASC ")
    fun getPagesDocument(documentId : Int) : LiveData<List<Page>>
}