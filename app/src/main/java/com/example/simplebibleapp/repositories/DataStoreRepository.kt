package com.example.simplebibleapp.repositories

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.simplebibleapp.dataStore
import com.example.simplebibleapp.readBibleData.DEFAULT_TRANSLATION
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStoreRepository(val context: Context) {
    val dataStore = context.dataStore

    // book is indexed from 0 (Genesis) to 65 (Revelation)
    val BOOK_INDEX_KEY = intPreferencesKey("bookindex")
    val CHAPTER_KEY = intPreferencesKey("chapter")
    val ZOOM_KEY = floatPreferencesKey("zoom")
    val TRANSLATION_KEY = stringPreferencesKey("translation")

    suspend fun saveBookIndexToDataStore(bookindex: Int) {
        dataStore.edit { preferences ->
            preferences[BOOK_INDEX_KEY] = bookindex
        }
    }
    suspend fun saveChapterToDataStore(chapter: Int) {
        dataStore.edit { preferences ->
            preferences[CHAPTER_KEY] = chapter
        }
    }
    suspend fun saveTranslationToDataStore(translation: String) {
        dataStore.edit { preferences ->
            preferences[TRANSLATION_KEY] = translation
        }
    }
    suspend fun getBookIndexFromDataStore(): Int {
        return dataStore.data.map { preferences ->
            preferences[BOOK_INDEX_KEY] ?: getFirstbook_Index()
        }.first()
    }
    suspend fun getChapterFromDataStore(): Int {
        return dataStore.data.map { preferences ->
            preferences[CHAPTER_KEY] ?: 1
        }.first()
    }
    suspend fun getTranslationFromDataStore() : String {
        return dataStore.data.map { preferences ->
            preferences[TRANSLATION_KEY] ?: DEFAULT_TRANSLATION
        }.first()
    }

    suspend fun saveZoomToDataStore(zoom: Float) {
        dataStore.edit { preferences ->
            preferences[ZOOM_KEY] = zoom
        }
    }
    suspend fun getZoomFromDataStore(): Float {
        return dataStore.data.map { preferences ->
            preferences[ZOOM_KEY] ?: 1f
        }.first()
    }
    fun getFirstbook_Index() : Int {
        return 0
    }
}