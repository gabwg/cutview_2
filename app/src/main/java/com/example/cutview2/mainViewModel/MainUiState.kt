package com.example.cutview2.mainViewModel

import com.example.cutview2.readBibleData.ReadBibleData

data class MainUiState(
    var book_index : Int = 0,
    var chapter : Int = 0,
    var zoom : Float = 1f,
    var readData: ReadBibleData
)