package com.droidcon.simplejokes.core.data

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.droidcon.simplejokes.core.domain.Languages
import com.droidcon.simplejokes.core.domain.datasource.AppLanguage
import com.droidcon.simplejokes.core.domain.datasource.AppTheme
import com.droidcon.simplejokes.core.domain.datasource.PreferencesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesDataSourceImpl(
    private val vault: Vault
) : PreferencesDataSource {

    private object Keys {
        val THEME = stringPreferencesKey("pref_theme") // Use DataStore key type
        val LANGUAGE = stringPreferencesKey("pref_language") // Use DataStore key type
    }

    override suspend fun putTheme(value: AppTheme) {
        vault.dataStore.edit { preferences ->
            preferences[Keys.THEME] = value
        }
    }

    override fun getTheme(): Flow<AppTheme> = vault.dataStore.data
        .map { preferences ->
            preferences[Keys.THEME] ?: ""
        }


    override suspend fun putLanguage(value: AppLanguage) {
        vault.dataStore.edit { preferences ->
            preferences[Keys.LANGUAGE] = value
        }
    }

    override fun getLanguage(): Flow<AppLanguage> = vault.dataStore.data
        .map { preferences ->
            preferences[Keys.LANGUAGE] ?: Languages.ENGLISH.languageTag
        }
}