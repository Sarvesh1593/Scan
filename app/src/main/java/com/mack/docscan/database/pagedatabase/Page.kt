package com.mack.docscan.database.pagedatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pages")
data class Page(
    @PrimaryKey(autoGenerate = true)
    val pageId: Int =0,
    var documentId: Long,
    val path: String
)