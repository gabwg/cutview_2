package com.example.cutview2.readBibleData

import android.content.Context
import com.example.cutview2.R
import com.example.cutview2.dataClasses.BookDetails


val en_translations = arrayOf("esv", "kj", "niv")
val translationIds = mapOf(
    "esv" to R.xml.beblia_englishesvbible,
    "niv" to R.xml.beblia_englishnivbible,
    "kj" to R.xml.beblia_englishkjbible)

/*
* Returns a ReadBibleData that uses a xml file sourced from https://github.com/Beblia/Holy-Bible-XML-Format
* supports esv, kj and niv
* */
class ReadBebliaData(context: Context, translation: String) : ReadBibleData {
    val resources = context.resources

    var language = ""
    var xmlId = 0
    override fun getBooknamesList(): List<String> {
        return resources.getStringArray(R.array.booknames_en).toList()
    }

    override fun getChapterCount(bookname: String): Int {
        val bookIndex = resources.getStringArray(R.array.booknames_en).indexOf(bookname)
        return getChapterCount(bookIndex)
    }

    override fun getChapterCount(bookIndex: Int): Int {
        return resources.getIntArray(R.array.chapternumbers)[bookIndex]
    }

    override fun getChapterFromBook(bookDetails: BookDetails, chapter: Int): List<String> {
        TODO("Not yet implemented")
    }

    override fun getLanguage(): String {
        TODO("Not yet implemented")
    }
    init {
        val _translation = translation.lowercase()
        if (_translation in en_translations) {
            language = "en"
        }
        xmlId = translationIds[_translation] ?: xmlId
    }
}