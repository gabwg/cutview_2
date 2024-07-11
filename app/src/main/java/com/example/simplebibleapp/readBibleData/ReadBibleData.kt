package com.example.simplebibleapp.readBibleData

import com.example.simplebibleapp.dataClasses.BookDetails

interface ReadBibleData {

    // get booknames (display) as an array
    fun getBooknamesList() : List<String>
    // get chapter count of a given book, name should be in the booknames array
    fun getChapterCount(bookname : String) : Int
    fun getChapterCount(bookIndex : Int) : Int
    // get chapter. list of strings, each string is a verse, if chapter is greater than book chapter count then reset back to first chapter
    fun getChapterFromBook(bookDetails : BookDetails, chapter: Int) : List<String>
    fun getLanguage(): String
    // gets the name of the translation
    fun getTranslationName(): String
}