package com.example.simplebibleapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.simplebibleapp.dataClasses.LocalisedPromptIds
import com.example.simplebibleapp.helpers.HelperFunctions
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





