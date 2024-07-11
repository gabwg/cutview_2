package com.example.simplebibleapp.mainViewModel

import com.example.simplebibleapp.readBibleData.ReadBibleData

data class MainUiState(
    var book_index : Int = 0,
    var chapter : Int = 0,
    var zoom : Float = 1f,
    var readData: ReadBibleData

)