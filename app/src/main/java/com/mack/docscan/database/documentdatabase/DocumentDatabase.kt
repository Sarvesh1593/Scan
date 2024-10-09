package com.mack.docscan.database.documentdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Document::class], version = 2, exportSchema = false)
abstract class DocumentDatabase : RoomDatabase() {

    abstract fun documentDao() : DocumentDao

    companion object {

        @Volatile
        private var INSTANCE: DocumentDatabase? = null

        fun getInstance(context: Context): DocumentDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        DocumentDatabase::class.java,
                        "document_db"
                    ).addMigrations(MIGRATION_1_2).build()
                }
            }
            return INSTANCE!!
        }

         val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE documents ADD COLUMN imagePath TEXT")
            }
        }
    }
}

