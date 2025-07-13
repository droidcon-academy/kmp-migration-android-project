package com.droidcon.simplejokes

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.droidcon.simplejokes.core.domain.datasource.PreferencesDataSource
import com.droidcon.simplejokes.core.presentation.Localization
import com.droidcon.simplejokes.core.presentation.SetSystemBarAppearance
import com.droidcon.simplejokes.core.presentation.SnackbarManager
import com.droidcon.simplejokes.core.presentation.ui.theme.SimpleJokesTheme
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.flow.distinctUntilChanged

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var preferencesDataSource: PreferencesDataSource

    @Inject
    lateinit var localization: Localization

    @Inject
    lateinit var snackbarManager: SnackbarManager


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            JokesThemeManager(
                preferencesDataSource = preferencesDataSource,
            ) {
                AppEffectHost(
                    localization = localization,
                    preferencesDataSource = preferencesDataSource,
                    snackbarManager = snackbarManager
                ) { snackbarHostState ->
                    Scaffold(
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                    ) {
                        NavigationRoot()
                    }
                }
            }
        }
    }
}

@Composable
private fun JokesThemeManager(preferencesDataSource: PreferencesDataSource, content: @Composable () -> Unit) {
    val themePreference by preferencesDataSource.getTheme().collectAsStateWithLifecycle("")
    val systemIsDark = isSystemInDarkTheme()

    val useDarkTheme = remember(themePreference, systemIsDark) {
        when (themePreference.uppercase()) {
            "DARK" -> true
            "LIGHT" -> false
            else -> systemIsDark
        }
    }

    SimpleJokesTheme(darkTheme = useDarkTheme) {
        SetSystemBarAppearance(darkTheme = useDarkTheme)
        content() // Render the rest of the app inside the theme
    }
}

@Composable
private fun AppEffectHost(
    localization: Localization,
    preferencesDataSource: PreferencesDataSource,
    snackbarManager: SnackbarManager,
    content: @Composable (snackbarHostState: SnackbarHostState) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Effect for handling language changes
    LaunchedEffect(localization, preferencesDataSource) {
        preferencesDataSource.getLanguage()
            .distinctUntilChanged()
            .collect { savedLanguageTag ->
                localization.updateLocale(savedLanguageTag)
            }
    }

    // Effect for handling snackbar messages
    LaunchedEffect(snackbarManager) {
        snackbarManager.messages.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    // Render the content, passing down the snackbar state
    content(snackbarHostState)
}