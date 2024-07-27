package com.example.simplebibleapp.repositories

import androidx.annotation.WorkerThread
import com.example.simplebibleapp.dataClasses.Selection
import com.example.simplebibleapp.logDb.HistoryDao
import com.example.simplebibleapp.logDb.HistoryRecord
import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class HistoryRepository(private val historyDao: HistoryDao) {
    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val getAll: Flow<List<HistoryRecord>> = historyDao.getAll()
    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(selection: Selection) {
        historyDao.insert(HistoryRecord(id = 0, selection = selection))
    }
    @WorkerThread
    suspend fun deleteAll() {
        historyDao.deleteAll()
    }

}