package com.example.simplebibleapp.mainViewModel

import com.example.simplebibleapp.dataClasses.DisplayConfig
import com.example.simplebibleapp.dataClasses.Selection

data class MainUiState(
    val displayConfig: DisplayConfig,
    val selection: Selection,
)