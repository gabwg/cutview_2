package com.example.simplebibleapp.mainViewModel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.simplebibleapp.readBibleData.ReadBibleDataFactory

class MainViewModelFactory(val readBibleDataFactory: ReadBibleDataFactory, val dataStore: DataStore<Preferences>) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(readBibleDataFactory = readBibleDataFactory, dataStore) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}