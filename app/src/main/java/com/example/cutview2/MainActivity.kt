package com.example.cutview2

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.cutview2.dataClasses.BookDetails
import com.example.cutview2.dataClasses.LocalisedPromptIds
import com.example.cutview2.helpers.HelperFunctions
import com.example.cutview2.mainAppComponents.Selector
import com.example.cutview2.mainAppComponents.TextBody
import com.example.cutview2.mainViewModel.MainViewModel
import com.example.cutview2.mainViewModel.MainViewModelFactory
import com.example.cutview2.readBibleData.ReadBibleDataFactory
import com.example.cutview2.ui.theme.CUTView2Theme

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
// 1x zoom font size
val DEFAULT_FONT_SIZE = 16.sp

val localisedPromptIds : Map<String, LocalisedPromptIds> = HelperFunctions().getLocalisedPromptIds()

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

    val readBibleDataFactory = ReadBibleDataFactory(context)

    val factory = MainViewModelFactory(readBibleDataFactory.get("CUV"), dataStore)
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
    val bookIndex = mainUiState.book_index

    val bookDetails = BookDetails(
        bookName = mainViewModel.getBookname(bookIndex),
        bookIndex = bookIndex,
        chapterCount = mainViewModel.getBookChapterCount(),
        language = mainViewModel.getLanguage())

    CUTView2Ui(
        bookDetails = bookDetails,
        chapter = mainUiState.chapter,
        booknameList = { mainViewModel.getBooknamesList() },
        setBookIndex = { b -> mainViewModel.setBookIndex(b) },
        setChapter = { c -> mainViewModel.setChapter(c) },
        getChapterFromBook = { b, c -> mainViewModel.getChapterFromBook(b, c) },
        setTranslation = { t -> mainViewModel.setReadBibleData(readBibleDataFactory.get(t)) },
        onZoom = { f -> onZoom(f) },
        textStyle = textStyle
    )

}

@Composable
fun CUTView2Ui(bookDetails : BookDetails,
               chapter : Int,
               booknameList: () -> List<String>,
               setBookIndex: (Int) -> Unit,
               setChapter: (Int) -> Unit,
               getChapterFromBook: (BookDetails, Int) -> List<String>,
               setTranslation: (String) -> Unit = {},
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
                bookDetails = bookDetails,
                chapter = chapter,
                bookNameList = booknameList,
                setBookIndex = setBookIndex,
                setChapter = setChapter,
                textStyle = textStyle
            )
            Button(onClick = { setTranslation("NIV") }, modifier = Modifier.padding(16.dp)) {
                Text("ChangeTranslation")
            }

            TextBody(
                getChapterFromBook(bookDetails, chapter),
                onZoom,
                textStyle = textStyle
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookChapterSelectors(
    bookDetails: BookDetails, chapter: Int,
    bookNameList: () -> List<String>,
    setBookIndex: (Int) -> Unit,
    setChapter: (Int) -> Unit,
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

        BookSelector(bookDetails, bookNameList, setBookIndex, textStyle)
        ChapterSelector(bookDetails, chapter, setChapter, textStyle)

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterSelector(bookDetails: BookDetails, chapter: Int, setChapter: (Int) -> Unit, textStyle: TextStyle) {
    val listItems = (1..bookDetails.chapterCount).toList()
    Selector(
        listItems = listItems,
        currentItem = chapter,
        dialogTitle = stringResource(localisedPromptIds[bookDetails.language]?.chapterPrompt ?: R.string.chapter_prompt_en),
        updateFunc = setChapter,
        textStyle = textStyle
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookSelector(bookDetails: BookDetails, bookNameList: () -> List<String>, setBookIndex: (Int) -> Unit, textStyle: TextStyle) {
    Selector(
        listItems = bookNameList(),
        currentItem = bookNameList()[bookDetails.bookIndex],
        buttonLabel = bookDetails.bookName,
        dialogTitle = stringResource(localisedPromptIds[bookDetails.language]?.bookPrompt ?: R.string.book_prompt_en),
        updateFunc = { b -> setBookIndex(bookNameList().indexOf(b)) },
        textStyle = textStyle
    )
}


@Preview(showBackground = true)
@Composable
fun CUTView2UiPreview() {
    CUTView2Ui(
        bookDetails = BookDetails("bookname 1", 0, 10,"en"),
        chapter = 1,
        booknameList = { listOf("bookname1", "bookname2") },
        setBookIndex = { },
        setChapter = { },
        getChapterFromBook = { b, c -> listOf("verse1", "verse2", "verse3") },
        onZoom = {},
        textStyle = TextStyle(fontSize = 16.sp)
    )
}

@Preview(showBackground = true)
@Composable
fun TextBodyDefaultSizePreview() {
    TextBody(
        listVerses = listOf("verse1", "verse2", stringResource(R.string.book_prompt_tcn)),
        onZoom = {},
        textStyle = TextStyle(fontSize = 16.sp))
}
@Preview(showBackground = true)
@Composable
fun TextBodyBigSizePreview() {
    TextBody(
        listVerses = listOf("verse1", "verse2", stringResource(R.string.book_prompt_tcn)),
        onZoom = {},
        textStyle = TextStyle(fontSize = 48.sp))
}

@Preview(showBackground = true)
@Composable
fun BookChapterSelectorsPreview() {
    BookChapterSelectors(
        bookDetails = BookDetails("bookname 1", 0, 10,"en"),
        chapter = 1,
        bookNameList = { listOf("bookname1", "bookname2") },
        setBookIndex = {},
        setChapter = {},
        textStyle = TextStyle(fontSize = 16.sp)
    )
}
