package com.example.simplebibleapp.readBibleData

import android.content.Context


class ReadBibleDataFactory(val context: Context) {
    val translations = listOf("CUV", "NIV", "KJV", "ESV")
    val defaultTranslation = "NIV"
    /*
    Gets the ReadBibleData object for the given translation.
    Available translations: CUV, NIV
     */
    fun get(translation: String) : ReadBibleData {
        return when (translation) {
            "CUV" -> ReadCUTViewData(context)
            "NIV" -> ReadBebliaData(context, translation)
            "KJV" -> ReadBebliaData(context, translation)
            "ESV" -> ReadBebliaData(context, translation)

            else -> throw Exception("Translation not found")
        }
    }
}