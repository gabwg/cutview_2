package com.example.simplebibleapp.readBibleData

import android.content.Context
import com.example.simplebibleapp.R
import com.example.simplebibleapp.dataClasses.BookDetails
import org.xmlpull.v1.XmlPullParser


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

    private var language = ""
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
        /* use xml pull parser on the xml specified.
        start/end tags:
        - "testament" tag - effectively ignored as we are going by book, as testaments and book index in xml file are independent
        - "book" tag - get the book tag with "number"/index 0 field matching the desired book
        - "chapter" tag  - get the chapter with "number"/index 0 field matching the desired chapter
        - "verse" tag - get all verses under the chapter and put all the verses into a list to return
        */
        var parser : XmlPullParser = resources.getXml(xmlId)
        var eventType = parser.eventType

        val bookNumber = bookDetails.bookIndex + 1
        val chapterNumber = chapter

        // in a correctly formatted xml file inVerse is not necessary as text is always within verse tags
        // but this check is done anyway in case there is text next to a non verse tag
        var inVerse = false
        val verseList = mutableListOf<String>()
        var currBookNumber = bookNumber
        var currChapterNumber = chapterNumber

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                val tagname = parser.name.lowercase()
                if (tagname == "book") {
                    currBookNumber = parser.getAttributeValue(null, "number").toInt()
                } else if (tagname == "chapter") {
                    currChapterNumber = parser.getAttributeValue(null, "number").toInt()
                } else if (tagname == "verse") {
                    inVerse = true
                }
                // for cases including tagname testament do nothing

            }
            else if (eventType == XmlPullParser.TEXT
                && bookNumber == currBookNumber
                && chapterNumber == currChapterNumber
                && inVerse) {
                // add the text to the list of verses
                verseList.add(parser.text)
            }
            else if (eventType == XmlPullParser.END_TAG) {
                inVerse = false
            }
            eventType = parser.next()
        }

        return verseList
    }

    override fun getLanguage(): String {
        return language
    }
    init {
        val _translation = translation.lowercase()
        if (_translation in en_translations) {
            language = "en"
        }
        xmlId = translationIds[_translation] ?: xmlId
    }
}