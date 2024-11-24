package com.example.myfirstapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map

private val ComponentActivity.dataStore by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {

    private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkMode by remember { mutableStateOf(false) }

//            считать при запуске тему
            LaunchedEffect(Unit) {
                isDarkMode = readDarkModeSetting()
            }

            MaterialTheme(
                colorScheme = if (isDarkMode) darkColorScheme() else lightColorScheme()
            ) {
                Surface {
                    Column {
                        Text("Темная тема ${if (isDarkMode) "включена" else "выключена"}")
                        Button(onClick = {
                            isDarkMode = !isDarkMode
                            saveDarkModeSetting(isDarkMode)
                        }) {
                            Text("Сменить тему")
                        }
                    }
                }
            }
        }
    }

    // сделать темно тему
    private fun saveDarkModeSetting(enabled: Boolean) {
        lifecycleScope.launch {
            dataStore.edit { preferences ->
                preferences[DARK_MODE_KEY] = enabled //сохран
            }
        }
    }

    // светло тема
    private suspend fun readDarkModeSetting(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[DARK_MODE_KEY] ?: false
        }.first()
    }
}
