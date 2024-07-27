package com.example.simplebibleapp.logDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.simplebibleapp.dataClasses.SelectionConverters

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(HistoryRecord::class), version = 1, exportSchema = false)
@TypeConverters(SelectionConverters::class)
public abstract class HistoryRoomDatabase : RoomDatabase() {

    abstract fun HistoryDao(): HistoryDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: HistoryRoomDatabase? = null

        fun getDatabase(context: Context): HistoryRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HistoryRoomDatabase::class.java,
                    "history_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}

