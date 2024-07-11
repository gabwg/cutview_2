package com.example.cutview2

interface ReadBibleData {

    // get booknames (display) as an array
    fun getBooknamesList() : List<String>
    // get chapter count of a given book, name should be in the booknames array
    fun getChapterCount(bookname : String) : Int
    // get chapter. list of strings, each string is a verse
    fun getChapterFromBook(bookname: String, chapter: Int) : List<String>

}