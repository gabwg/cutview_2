package com.example.simplebibleapp.dataClasses

import com.example.simplebibleapp.readBibleData.DEFAULT_TRANSLATION

data class Selection(
    val chapter: Int = 0,
    val bookIndex: Int = 0,
    val translation: String = DEFAULT_TRANSLATION
)

// s = election
fun sChangeChapter(selection: Selection, newChapter: Int) : Selection {
    val bookIndex = selection.bookIndex
    val translation = selection.translation
    return Selection(newChapter, bookIndex, translation)
}

fun sChangeBookIndex(selection: Selection, newBookIndex: Int) : Selection {
    val chapter = selection.chapter
    val translation = selection.translation
    return Selection(chapter, newBookIndex, translation)
}

fun sChangeTranslation(selection: Selection, newTranslation: String) : Selection {
    val chapter = selection.chapter
    val bookIndex = selection.bookIndex
    return Selection(chapter, bookIndex, newTranslation)
}

data class SelectionSetter(
    val setChapter: (Int) -> Unit,
    val setBookIndex: (Int) -> Unit,
    val setTranslation: (String) -> Unit
)