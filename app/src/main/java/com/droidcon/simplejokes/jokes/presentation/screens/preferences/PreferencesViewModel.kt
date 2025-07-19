package com.droidcon.simplejokes.jokes.presentation.screens.preferences

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidcon.simplejokes.core.domain.datasource.PreferencesDataSource
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Immutable
data class PreferencesState(
    val selectedLanguage: String = "",
    val selectedTheme: String = "",
)

sealed interface PreferencesIntent {
    data class UpdateLanguage(val language: String) : PreferencesIntent
    data class UpdateTheme(val theme: String) : PreferencesIntent
}

class PreferencesViewModel(
    private val preferences: PreferencesDataSource
): ViewModel() {

    val state = combine(
        preferences.getLanguage(),
        preferences.getTheme()
    ) { language, theme ->
        PreferencesState(selectedLanguage = language, selectedTheme = theme)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = PreferencesState()
    )

    // Central function to handle all user intents
    fun onIntent(intent: PreferencesIntent) {
        when (intent) {
            is PreferencesIntent.UpdateLanguage -> updateLanguage(intent.language)
            is PreferencesIntent.UpdateTheme -> updateTheme(intent.theme)
        }
    }

    // Private function to handle language update logic
    private fun updateLanguage(language: String) {
        viewModelScope.launch {
            preferences.putLanguage(language)
        }
    }

    // Private function to handle theme update logic
    private fun updateTheme(theme: String) {
        viewModelScope.launch {
            preferences.putTheme(theme)
        }
    }

}