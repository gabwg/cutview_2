package com.example.simplebibleapp.dataClasses

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

public val LOWERLIMIT = 0.5f
public val UPPERLIMIT = 3f

class DisplayConfig(val defaultSize: Float, val zoom: Float) {
    fun getUiTextStyle(): TextStyle {
        // no zoom, use default size
        val fontSize = (defaultSize).sp
        return TextStyle(fontSize = fontSize)
    }
    fun getBodyTextStyle(): TextStyle {
        val fontSize = (defaultSize * zoom).sp
        return TextStyle(fontSize = fontSize)
    }
}
data class DisplayConfigSetter(
    val onZoom: (Float) -> Unit
)