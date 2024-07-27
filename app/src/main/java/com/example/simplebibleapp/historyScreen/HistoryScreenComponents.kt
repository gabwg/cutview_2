package com.example.simplebibleapp.historyScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun HistoryListItem(bookname: String, chapter: Int, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text("${bookname} ${chapter}") },
        //supportingContent = { Text(chapter.toString()) },
        //trailingContent = { Text(selection.translation.toString()) },
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = false,
                onClick = onClick,
                role = Role.Button

            )
    )

}
@Preview(showBackground = true)
@Composable
fun HistoryListItemPreview() {
    Column() {
        HistoryListItem("Genesis", 1, {})
        HistoryListItem("Revelation", 2, {})
    }
}