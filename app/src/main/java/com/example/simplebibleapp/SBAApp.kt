package com.example.simplebibleapp

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simplebibleapp.dataClasses.DisplayConfig
import com.example.simplebibleapp.dataClasses.DisplayConfigSetter
import com.example.simplebibleapp.dataClasses.Selection
import com.example.simplebibleapp.dataClasses.SelectionSetter
import com.example.simplebibleapp.mainViewModel.MainViewModel
import com.example.simplebibleapp.mainViewModel.MainViewModelFactory
import com.example.simplebibleapp.readBibleData.ReadBibleDataFactory
import com.example.simplebibleapp.repositories.BibleRepository
import com.example.simplebibleapp.repositories.DataStoreRepository
import com.example.simplebibleapp.repositories.LocalBibleRepository
import com.example.simplebibleapp.ui.NavBar

enum class SBAScreens(val label: String, val icon: ImageVector) {
    BibleReader("Bible", Icons.Filled.Book),
    History("History", Icons.Filled.History)//,
    //Favourites()
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SBAApp(activity: ComponentActivity) {
    val context = LocalContext.current
    val dataStore = context.dataStore

    val readBibleDataFactory = ReadBibleDataFactory(context)

    val factory = MainViewModelFactory(DataStoreRepository(context))
    val mainViewModel = ViewModelProvider(activity, factory)[MainViewModel::class.java]
    val mainUiState by mainViewModel.uiState.collectAsState()

    val displayConfigSetter = DisplayConfigSetter(
        onZoom = {x -> mainViewModel.onZoom(x, DataStoreRepository(context))}
    )
    val selectionSetter = SelectionSetter(
        setChapter = {x -> mainViewModel.setChapter(x, DataStoreRepository(context))},
        setBookIndex = {x -> mainViewModel.setBookIndex(x, DataStoreRepository(context))},
        setTranslation = {x -> mainViewModel.setTranslation(x, DataStoreRepository(context))}
    )

    SBAScreen(
        displayConfig = mainUiState.displayConfig,
        displayConfigSetter = displayConfigSetter,
        selection = mainUiState.selection,
        selectionSetter = selectionSetter,
        bibleRepository = LocalBibleRepository(context)
    )

}

@Composable
fun SBAScreen(
    displayConfig: DisplayConfig,
    displayConfigSetter: DisplayConfigSetter,
    selection: Selection,
    selectionSetter: SelectionSetter,
    bibleRepository: BibleRepository
) {
    val navController = rememberNavController()
    Scaffold(modifier = Modifier.fillMaxSize(),
        bottomBar = { NavBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = SBAScreens.BibleReader.name)
        {
            composable(SBAScreens.BibleReader.name) {
                BibleReaderScreen(
                    displayConfig = displayConfig,
                    displayConfigSetter = displayConfigSetter,
                    selection = selection,
                    selectionSetter = selectionSetter,
                    bibleRepository = bibleRepository,
                    innerPadding = innerPadding
                )
            }
            composable(SBAScreens.History.name) {
                HistoryScreen()
            }
        }
    }
}