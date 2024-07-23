package com.example.simplebibleapp.readBibleData

import android.content.Context

val TRANSLATIONS : List<String> = listOf("CUV", "NIV", "KJV", "ESV")
val DEFAULT_TRANSLATION : String = "NIV"

class ReadBibleDataFactory(val context: Context) {
    /*
    Gets the ReadBibleData object for the given translation.
    Available translations: CUV, NIV
     */
    fun get(translation: String) : ReadBibleData {
        return when (translation.uppercase()) {
            "CUV" -> ReadCUTViewData(context)
            "NIV" -> ReadBebliaData(context, translation)
            "KJV" -> ReadBebliaData(context, translation)
            "ESV" -> ReadBebliaData(context, translation)

            else -> throw Exception("Translation not found")
        }
    }
}