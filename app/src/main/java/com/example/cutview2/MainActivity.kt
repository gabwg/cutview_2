package com.example.cutview2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cutview2.ui.theme.CUTView2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CUTView2Theme {
                CUTView2App()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CUTView2App(mainViewModel: MainViewModel = MainViewModel(ReadCUTViewData(LocalContext.current))) {

    val mainUiState by mainViewModel.uiState.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize(),
    ) {
        innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            BookChapterSelectors(
                bookname = mainUiState.bookname,
                chapter = mainUiState.chapter,
                bookchaptercount = { mainViewModel.bookChapterCount() },
                booknamelist = { mainViewModel.getBooknamesList() },
                updateBookname = { b -> mainViewModel.updateBookname(b) },
                updateChapter = { c -> mainViewModel.updateChapter(c) }
            )
            TextBody(
                mainViewModel.getChapterFromBook(mainUiState.bookname, mainUiState.chapter)
            )
    }
    }

}

@Composable
fun TextBody(listverses : List<String>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(horizontal = 16.dp)

    ) {
        listverses.forEachIndexed { index, verse ->
            item {
                Row() {
                    Text(
                        text = (index + 1).toString(),
                        modifier = Modifier
                            .requiredWidth(28.dp)
                    )
                    Text(verse)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookChapterSelectors(
    bookname: String, chapter: Int,
    bookchaptercount: () -> Int,
    booknamelist: () -> List<String>,
    updateBookname: (String) -> Unit, updateChapter: (Int) -> Unit,
    modifier: Modifier = Modifier) {

    var expanded = true
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ){

        BookDropDownMenu(bookname, booknamelist, updateBookname)
        ChapterDropDownMenu(bookname, chapter, bookchaptercount, updateChapter)

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterDropDownMenu(bookname: String, chapter: Int,
                        bookchaptercount: () -> Int, updateChapter: (Int) -> Unit) {
    val menuItems = (1..bookchaptercount()).toList()
    var expanded by remember { mutableStateOf(false) }
    Box {
        // Button to toggle the menu
        ElevatedButton(onClick = { expanded = true }) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ){
                Text(chapter.toString())
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Chapter Dropdown",
                )
            }
        }
        // Dropdown menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            menuItems.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.toString()) },
                    onClick = {
                        // Handle menu item click
                        updateChapter(item)
                        expanded = false
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDropDownMenu(bookname: String,
                     booknamelist: () -> List<String>, updateBookname: (String) -> Unit) {
    val menuItems = booknamelist()
    var expanded by remember { mutableStateOf(false) }
    Box {
        // Button to toggle the menu
        ElevatedButton(onClick = { expanded = true }) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ){
                Text(bookname)
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Book Dropdown",
                )
            }
        }
        // Dropdown menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            menuItems.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        // Handle menu item click
                        updateBookname(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainAppPreview() {
    CUTView2App()
}
