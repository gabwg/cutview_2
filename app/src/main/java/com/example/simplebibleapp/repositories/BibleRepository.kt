package com.example.simplebibleapp.repositories

import android.content.Context
import com.example.simplebibleapp.dataClasses.Selection
import com.example.simplebibleapp.readBibleData.ReadBibleData
import com.example.simplebibleapp.readBibleData.ReadBibleDataFactory
import com.example.simplebibleapp.readBibleData.TRANSLATIONS

interface BibleRepository {
    fun getTranslations(): List<String>
    fun getBooknamesList(translation: String): List<String>
    fun getBookname(translation: String, bookIndex: Int): String
    fun getBookname(selection: Selection): String
    fun getChapterVerses(selection: Selection): List<String>
    fun getChapterCount(selection: Selection) : Int
    fun getLanguage(translation: String) : String
}
class LocalBibleRepository(context: Context) : BibleRepository {
    val readBibleDataFactory: ReadBibleDataFactory = ReadBibleDataFactory(context)
    override fun getTranslations(): List<String> {
        return TRANSLATIONS
    }
    override fun getBooknamesList(translation: String): List<String> {
        return getReadBibleData(translation).getBooknamesList()
    }

    override fun getBookname(translation: String, bookIndex: Int): String {
        return getBooknamesList(translation)[bookIndex] ?: "ERR"
    }

    override fun getBookname(selection: Selection): String {
        return getBookname(selection.translation, selection.bookIndex)
    }

    override fun getChapterVerses(selection: Selection): List<String> {
        return getReadBibleData(selection.translation)
            .getChapterFromBook(selection)
    }
    private fun getReadBibleData(translation: String): ReadBibleData {
        return readBibleDataFactory.get(translation)
    }

    override fun getChapterCount(selection: Selection): Int {
        return getReadBibleData(selection.translation).getChapterCount(selection.bookIndex)
    }

    override fun getLanguage(translation: String) : String {
        return getReadBibleData(translation).getLanguage()
    }
}

class DummyBibleRepository() : BibleRepository {
    override fun getTranslations(): List<String> {
        return listOf("T1", "T2", "T3")
    }

    override fun getBooknamesList(translation: String): List<String> {
        return listOf("B1", "B2", "B3")
    }

    override fun getBookname(translation: String, bookIndex: Int): String {
        return getBooknamesList(translation)[bookIndex]
    }
    override fun getBookname(selection: Selection): String {
        return getBookname(selection.translation, selection.bookIndex)
    }
    override fun getChapterCount(selection: Selection):Int {
        return 4
    }

    override fun getChapterVerses(selection: Selection): List<String> {
        return listOf("Verse 1", "Verse 2", "Verse 3")
    }

    override fun getLanguage(translation: String): String {
        return "en"
    }
}