package com.droidcon.simplejokes.core.domain.datasource

import kotlinx.coroutines.flow.Flow

typealias AppTheme = String
typealias AppLanguage = String

interface PreferencesDataSource {

    suspend fun putTheme(value: AppTheme)
    fun getTheme(): Flow<AppTheme>

    suspend fun putLanguage(value: AppLanguage)
    fun getLanguage(): Flow<AppLanguage>

}