package com.example.simplebibleapp.helpers

import com.example.simplebibleapp.R
import com.example.simplebibleapp.dataClasses.LocalisedPromptIds

class HelperFunctions() {
    // Map<(Language), Map(PromptType>
    fun getLocalisedPromptIds() : Map<String, LocalisedPromptIds>{
        return mapOf(
            "en" to LocalisedPromptIds(R.string.book_prompt_en, R.string.chapter_prompt_en),
            "tcn" to LocalisedPromptIds(R.string.book_prompt_tcn, R.string.chapter_prompt_tcn)
        )
    }

}