package com.example.simplebibleapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simplebibleapp.dataClasses.DisplayConfig
import com.example.simplebibleapp.dataClasses.DisplayConfigSetter
import com.example.simplebibleapp.dataClasses.Selection
import com.example.simplebibleapp.dataClasses.SelectionSetter
import com.example.simplebibleapp.mainAppComponents.Selector
import com.example.simplebibleapp.mainAppComponents.TextBody
import com.example.simplebibleapp.readBibleData.TRANSLATIONS
import com.example.simplebibleapp.repositories.BibleRepository
import com.example.simplebibleapp.repositories.DummyBibleRepository

@Composable
fun BibleReaderScreen(
    displayConfig: DisplayConfig,
    displayConfigSetter: DisplayConfigSetter,
    selection: Selection,
    selectionSetter: SelectionSetter,
    bibleRepository: BibleRepository,
    innerPadding: PaddingValues
) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BibleReaderSelectors(
            displayConfig = displayConfig,
            selection = selection,
            selectionSetter = selectionSetter,
            bibleRepository = bibleRepository
        )

        TextBody(
            bibleRepository.getChapterVerses(selection),
            displayConfigSetter.onZoom,
            textStyle = displayConfig.getBodyTextStyle()
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BibleReaderSelectors(
    displayConfig: DisplayConfig,
    selection: Selection,
    selectionSetter: SelectionSetter,
    bibleRepository: BibleRepository,
    modifier: Modifier = Modifier
) {

    var expanded = true
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ){

        BookSelector(selection, bibleRepository, selectionSetter.setBookIndex, displayConfig)
        TranslationSelector(selection, selectionSetter.setTranslation, displayConfig)
        ChapterSelector(selection, bibleRepository, selectionSetter.setChapter, displayConfig)

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterSelector(
    selection: Selection,
    bibleRepository: BibleRepository,
    setChapter: (Int) -> Unit,
    displayConfig: DisplayConfig) {
    val listItems = (1..bibleRepository.getChapterCount(selection)).toList()
    val language = bibleRepository.getLanguage(selection.translation)
    Selector(
        listItems = listItems,
        currentItem = selection.chapter,
        dialogTitle = stringResource(localisedPromptIds[language]?.chapterPrompt ?: R.string.chapter_prompt_en),
        updateFunc = setChapter,
        textStyle = displayConfig.getUiTextStyle()
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookSelector(
    selection: Selection,
    bibleRepository: BibleRepository,
    setBookIndex: (Int) -> Unit,
    displayConfig: DisplayConfig) {
    val language = bibleRepository.getLanguage(selection.translation)
    val bookNameList = bibleRepository.getBooknamesList(selection.translation)
    Selector(
        listItems = bookNameList,
        currentItem = bookNameList[selection.bookIndex],
        dialogTitle = stringResource(localisedPromptIds[language]?.bookPrompt ?: R.string.book_prompt_en),
        updateFunc = { b -> setBookIndex(bookNameList.indexOf(b)) },
        textStyle = displayConfig.getUiTextStyle()
    )
}
@Composable
fun TranslationSelector(
    selection: Selection,
    setTranslation: (String) -> Unit,
    displayConfig: DisplayConfig) {
    Selector(
        listItems = TRANSLATIONS,
        currentItem = selection.translation,
        dialogTitle = "Translation",
        updateFunc = {b -> setTranslation(b)},
        textStyle = displayConfig.getUiTextStyle()
    )
}

@Preview(showBackground = true)
@Composable
fun BibleReaderScreenPreview() {
    BibleReaderScreen(
        displayConfig = DisplayConfig(16f, 1f),
        displayConfigSetter = DisplayConfigSetter({}),
        selection = Selection(1, 0, "NIV"),
        selectionSetter = SelectionSetter({}, {}, {}),
        bibleRepository = DummyBibleRepository(),
        innerPadding = PaddingValues(0.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun SBASelectorsPreview() {
    BibleReaderSelectors(
        displayConfig = DisplayConfig(16f, 1f),
        selection = Selection(1, 0, "NIV"),
        selectionSetter = SelectionSetter({}, {}, {}),
        bibleRepository = DummyBibleRepository(),
        modifier = Modifier
    )
}
