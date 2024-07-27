package com.example.simplebibleapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simplebibleapp.dataClasses.Selection
import com.example.simplebibleapp.dataClasses.SelectionSetter
import com.example.simplebibleapp.historyScreen.HistoryListItem
import kotlinx.coroutines.launch

@Composable
fun HistoryScreen(
    selection: Selection,
    selectionSetter: SelectionSetter,
    insert: suspend (Selection) -> Unit,
    history: List<Selection> = emptyList(),
    getBookname: (String, Int) -> String = {_, x -> "Book $x"},
    deleteAll: suspend () -> Unit = {},
    goToBibleReader: () -> Unit = {}
    ) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.End
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Button(onClick = {
                coroutineScope.launch{
                    insert(selection)
                }
            }) {
                Text("HI")
            }
            OutlinedButton(onClick = {
                coroutineScope.launch{
                    deleteAll()
                }
            }) {
                Text("Clear")
            }
        }
        LazyColumn() {
            history.forEach {
                val bookIndex = it.bookIndex
                val chapter = it.chapter
                item {
                    HistoryListItem(
                        bookname = getBookname(selection.translation, bookIndex),
                        chapter = chapter,
                        onClick = {
                            selectionSetter.setBookIndex(bookIndex)
                            selectionSetter.setChapter(chapter)
                            goToBibleReader()
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreviewEmpty() {
    HistoryScreen(Selection(1, 1, "NIV"), insert = {}, selectionSetter = SelectionSetter({}, {}, {}), history = listOf())
}
@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    val history: List<Selection> = listOf(
        Selection(1, 2, "NIV"),
        Selection(3, 1, "NIV"),
        Selection(2, 1, "NIV"),
        Selection(1, 1, "NIV"),

    )
    HistoryScreen(Selection(1, 1, "NIV"), insert = {}, history = history, selectionSetter = SelectionSetter({}, {}, {}))
}
@Preview(showBackground = true)
@Composable
fun HistoryScreenPreviewTooManyItems() {
    val history: List<Selection> = listOf(
        Selection(1, 2, "NIV"),
        Selection(3, 1, "NIV"),
        Selection(2, 1, "NIV"),
        Selection(1, 1, "NIV"),
        Selection(1, 1, "NIV"),
        Selection(1, 1, "NIV"),
        Selection(1, 1, "NIV"),
        Selection(1, 1, "NIV"),
        Selection(1, 1, "NIV"),
        Selection(1, 1, "NIV"),
        Selection(1, 1, "NIV"),
        Selection(1, 1, "NIV"),
        Selection(1, 1, "NIV"),
        Selection(1, 1, "NIV"),
        Selection(1, 1, "NIV"),
        Selection(1, 1, "NIV"),
        Selection(1, 1, "NIV"),
        Selection(1, 1, "NIV"),
        Selection(1, 1, "NIV"),
        Selection(1, 2, "NIV"),

    )
    HistoryScreen(Selection(1, 1, "NIV"), insert = {}, history = history, selectionSetter = SelectionSetter({}, {}, {}))
}