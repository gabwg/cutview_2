package com.example.cutview2

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel(val readdata: ReadBibleData) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
    fun setBookname(bookname: String) {
        _uiState
    }
    fun getFirstbook() : String {
        return readdata.getBooknamesList()[0]
    }
    fun getBooknamesList() : List<String> {
        return readdata.getBooknamesList()
    }
    fun resetApp() {
        _uiState.value = MainUiState(bookname = getFirstbook(), chapter = 1)
    }
    fun bookChapterCount() : Int {
        return readdata.getChapterCount(_uiState.value.bookname)
    }
    fun updateBookname(bookname: String) {
        _uiState.update { currentState -> currentState.copy(bookname = bookname)}
    }
    fun updateChapter(chapter: Int) {
        _uiState.update { currentState -> currentState.copy(chapter = chapter)}
    }
    fun getChapterFromBook(bookname: String, chapter: Int) : List<String> {
        return readdata.getChapterFromBook(bookname, chapter)
    }
    init {
        resetApp()

    }
}