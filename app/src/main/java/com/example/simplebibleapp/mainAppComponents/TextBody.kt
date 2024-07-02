package com.example.simplebibleapp.mainAppComponents

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun TextBody(listVerses : List<String>, onZoom: (Float) -> (Unit), textStyle: TextStyle, modifier: Modifier = Modifier) {

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .pointerInput(Unit) {
                detectTransformGestures(
                    onGesture = { _, _, gestureZoom, _ ->
                        onZoom(gestureZoom)
                    }
                )
            }
    ) {
        listVerses.forEachIndexed { index, verse ->
            item {
                Row() {
                    Text(
                        text = (index + 1).toString(),
                        modifier = Modifier
                            .requiredWidth((1.75 * textStyle.fontSize.value).dp),
                        style = textStyle
                    )
                    Text(
                        text = verse,
                        style = textStyle
                    )
                }
            }
        }
    }
}
