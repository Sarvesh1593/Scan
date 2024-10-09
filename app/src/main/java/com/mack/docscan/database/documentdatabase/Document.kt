package com.mack.docscan.database.documentdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "documents")
data class Document(
    @PrimaryKey(autoGenerate = true)
    val documentId : Int = 0,
    val name : String,
    val imagePath : String,
    val pageCount : Int
) {
}
