package com.example.simplebibleapp

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import com.example.simplebibleapp.dataClasses.BookDetails
import com.example.simplebibleapp.dataClasses.LocalisedPromptIds
import com.example.simplebibleapp.helpers.HelperFunctions
import com.example.simplebibleapp.mainAppComponents.Selector
import com.example.simplebibleapp.mainAppComponents.TextBody
import com.example.simplebibleapp.mainViewModel.MainViewModel
import com.example.simplebibleapp.mainViewModel.MainViewModelFactory
import com.example.simplebibleapp.readBibleData.ReadBibleDataFactory
import com.example.simplebibleapp.ui.theme.SBATheme

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
// 1x zoom font size
val DEFAULT_FONT_SIZE = 16.sp

val localisedPromptIds : Map<String, LocalisedPromptIds> = HelperFunctions().getLocalisedPromptIds()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            SBATheme {
                SBAApp(this)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SBAApp(activity: ComponentActivity) {
    val context = LocalContext.current
    val dataStore = context.dataStore

    val readBibleDataFactory = ReadBibleDataFactory(context)

    val factory = MainViewModelFactory(readBibleDataFactory, dataStore)
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

    SBAUi(
        bookDetails = bookDetails,
        chapter = mainUiState.chapter,
        booknameList = { mainViewModel.getBooknamesList() },
        setBookIndex = { b -> mainViewModel.setBookIndex(b) },
        setChapter = { c -> mainViewModel.setChapter(c) },
        getChapterFromBook = { b, c -> mainViewModel.getChapterFromBook(b, c) },
        setTranslation = { t -> mainViewModel.setReadBibleData(t) },
        translations = mainViewModel.getTranslations(),
        currentTranslation = mainViewModel.getCurrentTranslation(),
        onZoom = { f -> onZoom(f) },
        textStyle = textStyle
    )

}

@Composable
fun SBAUi(bookDetails : BookDetails,
          chapter : Int,
          booknameList: () -> List<String>,
          setBookIndex: (Int) -> Unit,
          setChapter: (Int) -> Unit,
          getChapterFromBook: (BookDetails, Int) -> List<String>,
          translations: List<String>,
          currentTranslation: String,
          setTranslation: (String) -> Unit = {},
          onZoom: (Float) -> (Unit) = {},
          textStyle: TextStyle,
          selectorTextStyle: TextStyle = TextStyle(fontSize = 16.sp)
) {

    Scaffold(modifier = Modifier.fillMaxSize(),
    ) {
            innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            SBASelectors(
                bookDetails = bookDetails,
                chapter = chapter,
                bookNameList = booknameList,
                setBookIndex = setBookIndex,
                setChapter = setChapter,
                translations = translations,
                currentTranslation = currentTranslation,
                setTranslation = setTranslation,
                textStyle = selectorTextStyle
            )

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
fun SBASelectors(
    bookDetails: BookDetails, chapter: Int,
    bookNameList: () -> List<String>,
    setBookIndex: (Int) -> Unit,
    setChapter: (Int) -> Unit,
    translations: List<String>,
    currentTranslation: String,
    setTranslation: (String) -> Unit,
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
        TranslationSelector(translations, currentTranslation, setTranslation, textStyle)
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
@Composable
fun TranslationSelector(translations: List<String>, currentTranslation: String, setTranslation: (String) -> Unit, textStyle: TextStyle) {
    Selector(
      listItems = translations,
        currentItem = currentTranslation,
        buttonLabel = currentTranslation,
        dialogTitle = "Translation",
        updateFunc = {b -> setTranslation(b)},
        textStyle = textStyle
    )
}

@Preview(showBackground = true)
@Composable
fun SBAUiPreview() {
    SBAUi(
        bookDetails = BookDetails("bookname 1", 0, 10,"en"),
        chapter = 1,
        booknameList = { listOf("bookname1", "bookname2") },
        setBookIndex = { },
        setChapter = { },
        getChapterFromBook = { b, c -> listOf("verse1", "verse2", "verse3") },
        onZoom = {},
        translations = listOf("translation1", "translation2"),
        currentTranslation = "translation1",
        setTranslation = {},
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
fun SBASelectorsPreview() {
    SBASelectors(
        bookDetails = BookDetails("bookname 1", 0, 10,"en"),
        chapter = 1,
        bookNameList = { listOf("bookname1", "bookname2") },
        setBookIndex = {},
        setChapter = {},
        translations = listOf("translation1", "translation2"),
        currentTranslation = "translation1",
        setTranslation = {},
        textStyle = TextStyle(fontSize = 16.sp)
    )
}
