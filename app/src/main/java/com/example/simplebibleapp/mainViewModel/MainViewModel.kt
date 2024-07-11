package com.example.simplebibleapp.mainViewModel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import com.example.simplebibleapp.dataClasses.BookDetails
import com.example.simplebibleapp.readBibleData.ReadBibleData
import com.example.simplebibleapp.readBibleData.ReadBibleDataFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MainViewModel(val readBibleDataFactory: ReadBibleDataFactory, val dataStore: DataStore<Preferences>) : ViewModel() {
    lateinit var uiState: StateFlow<MainUiState>;

    lateinit var readData : ReadBibleData;
    // translation is used for 'display' purposes
    private lateinit var _uiState : MutableStateFlow<MainUiState>;

    // book is indexed from 0 (Genesis) to 65 (Revelation)
    val BOOK_INDEX_KEY = intPreferencesKey("bookindex")
    val CHAPTER_KEY = intPreferencesKey("chapter")
    val ZOOM_KEY = floatPreferencesKey("zoom")
    val TRANSLATION_KEY = stringPreferencesKey("translation")

    fun setReadBibleData(translationName: String) {
        readData = readBibleDataFactory.get(translationName)
        _uiState.update { currentState -> currentState.copy(readData = readData) }
        runBlocking {
            launch {
                saveTranslationToDataStore(translationName)
            }
        }
    }
    fun getFirstbook() : String {
        return readData.getBooknamesList()[0]
    }
    fun getFirstbook_Index() : Int {
        return 0
    }
    fun getBooknamesList() : List<String> {
        return readData.getBooknamesList()
    }
    fun getBookname(bookIndex: Int) : String {
        if (bookIndex < 0 || bookIndex > readData.getBooknamesList().size) {
            return readData.getBooknamesList()[0]
        }
        return readData.getBooknamesList()[bookIndex]
    }
    fun getLanguage() : String {
        return readData.getLanguage()
    }
    fun getTranslations(): List<String> {
        return readBibleDataFactory.translations
    }
    fun getCurrentTranslation(): String {
        return readData.getTranslationName()
    }
    fun resetApp() {
        _uiState.value = MainUiState(book_index = getFirstbook_Index(), chapter = 1, zoom = 1f, readData)
    }
    fun getBookChapterCount() : Int {
        return readData.getChapterCount(_uiState.value.book_index)
    }
    fun setBookIndex(bookIndex: Int) {
        _setBookIndex(bookIndex)
        _setChapter(1) // because all books have at least chapter 1
        runBlocking {
            launch {
                saveBookIndexToDataStore(bookIndex)
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
    private fun _setBookIndex(book_index: Int) {
        _uiState.update { currentState -> currentState.copy(book_index = book_index)}
    }
    private fun _setChapter(chapter: Int) {
        _uiState.update { currentState -> currentState.copy(chapter = chapter)}
    }
    fun getChapterFromBook(bookDetails: BookDetails, chapter: Int) : List<String> {
        return readData.getChapterFromBook(bookDetails, chapter)
    }

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
    suspend fun getTranslationFromDataStore(): String {
        return dataStore.data.map { preferences ->
            preferences[TRANSLATION_KEY] ?: readBibleDataFactory.defaultTranslation
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
        runBlocking {
            launch {
                // must initialise _uiState first
                readData = readBibleDataFactory.get(getTranslationFromDataStore())
                _uiState = MutableStateFlow(MainUiState(readData = readData))
                uiState = _uiState.asStateFlow()

                resetApp()
                _setBookIndex(getBookIndexFromDataStore())
                _setChapter(getChapterFromDataStore())
                _setZoom(getZoomFromDataStore())
            }
        }
    }

}
