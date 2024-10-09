package com.mack.docscan.database.pagedatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mack.docscan.database.documentdatabase.DocumentDatabase.Companion.MIGRATION_1_2

@Database(entities = [Page::class], version = 2, exportSchema = false)
abstract class PageDatabase : RoomDatabase() {
    abstract fun pageDao() : PageDao

    companion object{
        private var INSTANCE : PageDatabase? = null

        fun getInstance(context : Context) : PageDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PageDatabase::class.java,
                    "page_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}