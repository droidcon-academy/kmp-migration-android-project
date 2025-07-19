package com.droidcon.simplejokes.jokes.presentation.screens.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.droidcon.simplejokes.R
import com.droidcon.simplejokes.core.domain.Languages
import org.koin.androidx.compose.koinViewModel

@Composable
fun PreferencesScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: PreferencesViewModel = koinViewModel(),
    onGoBack: () -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    // Call the stateless screen composable
    PreferencesScreen(
        modifier = modifier,
        state = state.value,
        onIntent = viewModel::onIntent,
        onGoBack = onGoBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesScreen(
    modifier: Modifier = Modifier,
    state: PreferencesState,
    onIntent: (PreferencesIntent) -> Unit,
    onGoBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.preferences_title)) },
                actions = {
                    IconButton(onClick = onGoBack) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- Language Section ---
            PreferenceSectionTitle(stringResource(R.string.preference_language))
            Languages.entries.forEach { language ->
                PreferenceOption(
                    label = language.displayName,
                    value = language.languageTag,
                    currentSelection = state.selectedLanguage,
                    onSelected = { onIntent(PreferencesIntent.UpdateLanguage(language.languageTag)) }
                )
            }

            HorizontalDivider()

            // --- Theme Section ---
            PreferenceSectionTitle(stringResource(R.string.preference_theme))
            PreferenceOption( // Use generic helper
                label = stringResource(R.string.theme_label_default),
                value = "",
                currentSelection = state.selectedTheme,
                onSelected = { onIntent(PreferencesIntent.UpdateTheme("")) }
            )
            PreferenceOption(
                label = stringResource(R.string.theme_lable_light),
                value = "LIGHT",
                currentSelection = state.selectedTheme, // Use state
                onSelected = { onIntent(PreferencesIntent.UpdateTheme("LIGHT")) }
            )
            PreferenceOption(
                label = stringResource(R.string.theme_label_dark),
                value = "DARK",
                currentSelection = state.selectedTheme, // Use state
                onSelected = { onIntent(PreferencesIntent.UpdateTheme("DARK")) }
            )
        }
    }
}

@Composable
private fun PreferenceSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun PreferenceOption(
    label: String,
    value: String,
    currentSelection: String,
    onSelected: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelected),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = (value == currentSelection),
            onClick = null
        )
        Spacer(Modifier.width(8.dp))
        Text(label)
    }
}
