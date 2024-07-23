package com.example.simplebibleapp.mainViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.simplebibleapp.repositories.DataStoreRepository

class MainViewModelFactory(val dataStoreRepository: DataStoreRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(dataStoreRepository) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}