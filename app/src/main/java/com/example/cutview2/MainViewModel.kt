package com.example.cutview2

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MainViewModel(val readdata: ReadBibleData, val dataStore: DataStore<Preferences>) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    val BOOKNAME_KEY = stringPreferencesKey("bookname")
    val CHAPTER_KEY = intPreferencesKey("chapter")
    val ZOOM_KEY = floatPreferencesKey("zoom")

    fun getFirstbook() : String {
        return readdata.getBooknamesList()[0]
    }
    fun getBooknamesList() : List<String> {
        return readdata.getBooknamesList()
    }
    fun resetApp() {
        _uiState.value = MainUiState(bookname = getFirstbook(), chapter = 1, zoom = 1f)
    }
    fun bookChapterCount() : Int {
        return readdata.getChapterCount(_uiState.value.bookname)
    }
    fun setBookname(bookname: String) {
        _setBookname(bookname)
        _setChapter(1) // because all books have at least chapter 1
        runBlocking {
            launch {
                saveBooknameToDataStore(bookname)
            }
        }
    }
    fun setChapter(chapter: Int) {
        _setChapter(chapter)
        runBlocking {
            launch {
                saveChapterToDataStore(chapter)
            }
        }

    }
    // non public facing versions, which do not save to datastore
    private fun _setBookname(bookname: String) {
        _uiState.update { currentState -> currentState.copy(bookname = bookname)}
    }
    private fun _setChapter(chapter: Int) {
        _uiState.update { currentState -> currentState.copy(chapter = chapter)}
    }
    fun getChapterFromBook(bookname: String, chapter: Int) : List<String> {
        return readdata.getChapterFromBook(bookname, chapter)
    }

    suspend fun saveBooknameToDataStore(bookname: String) {
        dataStore.edit { preferences ->
            preferences[BOOKNAME_KEY] = bookname
        }
    }
    suspend fun saveChapterToDataStore(chapter: Int) {
        dataStore.edit { preferences ->
            preferences[CHAPTER_KEY] = chapter
        }
    }
    suspend fun getBooknameFromDataStore(): String {
        return dataStore.data.map { preferences ->
            preferences[BOOKNAME_KEY] ?: getFirstbook()
        }.first()
    }
    suspend fun getChapterFromDataStore(): Int {
        return dataStore.data.map { preferences ->
            preferences[CHAPTER_KEY] ?: 1
        }.first()
    }

    fun setZoom(zoom: Float) {
        _setZoom(zoom)
        runBlocking {
            launch {
                saveZoomToDataStore(zoom)
            }
        }
    }

    private suspend fun saveZoomToDataStore(zoom: Float) {
        dataStore.edit { preferences ->
            preferences[ZOOM_KEY] = zoom
        }
    }
    suspend fun getZoomFromDataStore(): Float {
        return dataStore.data.map { preferences ->
            preferences[ZOOM_KEY] ?: 1f
        }.first()
    }
    private fun _setZoom(zoom: Float) {
        _uiState.update { currentState -> currentState.copy(zoom = zoom)}
    }

    init {
        resetApp()
        runBlocking {
            launch {
                _setBookname(getBooknameFromDataStore())
                _setChapter(getChapterFromDataStore())
                _setZoom(getZoomFromDataStore())
            }
        }
    }

}
