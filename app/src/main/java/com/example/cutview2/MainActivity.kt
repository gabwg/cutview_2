package com.example.cutview2

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.cutview2.ui.theme.CUTView2Theme
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
// 1x zoom font size
val DEFAULT_FONT_SIZE = 16.sp
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            CUTView2Theme {
                CUTView2App(this)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CUTView2App(activity: ComponentActivity) {
    val context = LocalContext.current
    val dataStore = context.dataStore
    val factory = MainViewModelFactory(ReadCUTViewData(context), dataStore)
    val mainViewModel = ViewModelProvider(activity, factory)[MainViewModel::class.java]
    val mainUiState by mainViewModel.uiState.collectAsState()

    // for adjusting fontsize of text
    var zoom = mainUiState.zoom
    fun onZoom(gestureZoom: Float) {
        val newScale = zoom * gestureZoom
        if (newScale > 0.5f && newScale < 3f) {
            // clamp zoom
            zoom = newScale
            mainViewModel.setZoom(newScale)
        }
    }

    val fontSize = (DEFAULT_FONT_SIZE.value * zoom).sp
    val textStyle = TextStyle(fontSize = fontSize)


    CUTView2Ui(
        bookname = mainUiState.bookname,
        chapter = mainUiState.chapter,
        bookchaptercount = { mainViewModel.bookChapterCount() },
        booknamelist = { mainViewModel.getBooknamesList() },
        updateBookname = { b -> mainViewModel.setBookname(b) },
        updateChapter = { c -> mainViewModel.setChapter(c) },
        getChapterFromBook = { b, c -> mainViewModel.getChapterFromBook(b, c) },
        onZoom = { f -> onZoom(f) },
        textStyle = textStyle
    )

}

@Composable
fun CUTView2Ui(bookname : String,
               chapter : Int,
               bookchaptercount: () -> Int,
               booknamelist: () -> List<String>,
               updateBookname: (String) -> Unit,
               updateChapter: (Int) -> Unit,
               getChapterFromBook: (String, Int) -> List<String>,
               onZoom: (Float) -> (Unit) = {},
               textStyle: TextStyle
) {

    Scaffold(modifier = Modifier.fillMaxSize(),
    ) {
            innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            BookChapterSelectors(
                bookname = bookname,
                chapter = chapter,
                bookchaptercount = bookchaptercount,
                booknamelist = booknamelist,
                updateBookname = updateBookname,
                updateChapter = updateChapter,
                textStyle = textStyle
            )
            TextBody(
                getChapterFromBook(bookname, chapter),
                onZoom,
                textStyle = textStyle
            )
        }
    }
}

@Composable
fun TextBody(listverses : List<String>, onZoom: (Float) -> (Unit), textStyle: TextStyle, modifier: Modifier = Modifier) {

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
        listverses.forEachIndexed { index, verse ->
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookChapterSelectors(
    bookname: String, chapter: Int,
    bookchaptercount: () -> Int,
    booknamelist: () -> List<String>,
    updateBookname: (String) -> Unit, updateChapter: (Int) -> Unit,
    textStyle: TextStyle,
    modifier: Modifier = Modifier) {

    var expanded = true
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ){

        BookSelector(bookname, booknamelist, updateBookname, textStyle)
        ChapterSelector(bookname, chapter, bookchaptercount, updateChapter, textStyle)

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterSelector(bookname: String, chapter: Int,
                        bookchaptercount: () -> Int, updateChapter: (Int) -> Unit, textStyle: TextStyle) {
    val menuItems = (1..bookchaptercount()).toList()
    var expanded by remember { mutableStateOf(false) }
    Box {
        // Button to toggle the menu
        ElevatedButton(onClick = { expanded = true }) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ){
                Text(chapter.toString(), style = textStyle)
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Chapter Dropdown",
                )
            }
        }
        if (expanded) {
            SelectorDialog(
                title = stringResource(R.string.chapter_prompt),
                current = chapter.toString(),
                selectorItems = menuItems.map { it.toString() },
                updateFunc = { c -> updateChapter(c.toInt())},
                onDismissRequest = { expanded = false },
                textStyle = textStyle
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookSelector(bookname: String,
                     booknamelist: () -> List<String>, updateBookname: (String) -> Unit, textStyle: TextStyle) {
    val menuItems = booknamelist()
    var expanded by remember { mutableStateOf(false) }


    Box {
        // Button to toggle the menu
        ElevatedButton(onClick = { expanded = true }) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ){
                Text(bookname, style = textStyle)
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Book Dropdown",
                )
            }
        }
        // Dialog launch here
        if (expanded) {
            SelectorDialog(
                title = stringResource(id = R.string.book_prompt),
                current = bookname,
                selectorItems = menuItems,
                updateFunc = updateBookname,
                onDismissRequest = { expanded = false },
                textStyle = textStyle
            )
        }
    }
}


@Preview
@Composable
fun SelectorDialogPreview() {
    SelectorDialog(
        title = "Title",
        current = "Option 2",
        selectorItems = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5", "Option 6", "Option 7", "Option 8"),
        updateFunc = {},
        onDismissRequest = {},
        textStyle = TextStyle(fontSize = 16.sp)
    )
}
@Composable
fun SelectorDialog(
    title : String,
    current: String,
    selectorItems: List<String>,
    updateFunc: (String) -> Unit,
    onDismissRequest: () -> Unit,
    textStyle: TextStyle
) {
    val state = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(8.dp))
                Divider()
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp),
                    state = state
                ) {
                    var currentIndex = 0
                    selectorItems.forEachIndexed() {index, selectorItem ->
                        if (current == selectorItem) {
                            currentIndex = index
                        }
                        item {
                            ListItem(
                                headlineContent = { Text(selectorItem, style = textStyle) },
                                leadingContent = {RadioButton(selected = (current == selectorItem), onClick = null)},
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = (current == selectorItem),
                                        onClick = {
                                            updateFunc(selectorItem)
                                            onDismissRequest()
                                        },
                                        role = Role.RadioButton
                                    ),
                                colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                            )
                        }
                    }
                    coroutineScope.launch{
                        state.scrollToItem(index = currentIndex)
                    }
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {

                    TextButton(
                        modifier = Modifier,
                        onClick = { onDismissRequest() }){
                        Text("Dismiss", style = textStyle)
                    }
                }

            }
        }
    }

}




@Preview(showBackground = true)
@Composable
fun CUTView2UiPreview() {
    CUTView2Ui(
        bookname = "bookname 1",
        chapter = 1,
        bookchaptercount = { 10 },
        booknamelist = { listOf("bookname1", "bookname2") },
        updateBookname = {},
        updateChapter = {},
        getChapterFromBook = { b, c -> listOf("verse1", "verse2", "verse3") },
        onZoom = {},
        textStyle = TextStyle(fontSize = 16.sp)
    )
}

@Preview(showBackground = true)
@Composable
fun TextBodyDefaultSizePreview() {
    TextBody(
        listverses = listOf("verse1", "verse2", stringResource(R.string.book_prompt)),
        onZoom = {},
        textStyle = TextStyle(fontSize = 16.sp))
}
@Preview(showBackground = true)
@Composable
fun TextBodyBigSizePreview() {
    TextBody(
        listverses = listOf("verse1", "verse2", stringResource(R.string.book_prompt)),
        onZoom = {},
        textStyle = TextStyle(fontSize = 48.sp))
}

@Preview(showBackground = true)
@Composable
fun BookChapterSelectorsPreview() {
    BookChapterSelectors(
        bookname = "bookname",
        chapter = 1,
        bookchaptercount = { 10 },
        booknamelist = { listOf("bookname1", "bookname2") },
        updateBookname = {},
        updateChapter = {},
        textStyle = TextStyle(fontSize = 16.sp)
    )
}
