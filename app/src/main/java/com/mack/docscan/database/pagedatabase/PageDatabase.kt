package com.mack.docscan.database.pagedatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Page::class], version = 3, exportSchema = false)
abstract class PageDatabase : RoomDatabase() {
    abstract fun pageDao(): PageDao

    companion object {
        private var INSTANCE: PageDatabase? = null

        fun getInstance(context: Context): PageDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PageDatabase::class.java,
                    "page_db"
                )
                    .addMigrations(MIGRATION_2_3) // Add the migration here
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Migration from version 2 to 3
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE pages ADD COLUMN documentName TEXT NOT NULL DEFAULT ''")
            }
        }
    }
}
