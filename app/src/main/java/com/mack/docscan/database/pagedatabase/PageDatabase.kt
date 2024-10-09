package com.mack.docscan.database.pagedatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Page::class], version = 1, exportSchema = false)
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
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}