package com.example.simplebibleapp.readBibleData

import android.content.Context


class ReadBibleDataFactory(val context: Context) {
    /*
    Gets the ReadBibleData object for the given translation.
    Available translations: CUV
     */
    fun get(translation: String) : ReadBibleData {
        return when (translation) {
            "CUV" -> ReadCUTViewData(context)
            "NIV" -> ReadBebliaData(context, translation)

            else -> throw Exception("Translation not found")
        }
    }
}