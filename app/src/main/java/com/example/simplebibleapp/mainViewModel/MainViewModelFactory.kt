package com.example.simplebibleapp.mainViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.simplebibleapp.repositories.DataStoreRepository
import com.example.simplebibleapp.repositories.HistoryRepository

class MainViewModelFactory(val dataStoreRepository: DataStoreRepository, val historyRepository: HistoryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(dataStoreRepository, historyRepository) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}