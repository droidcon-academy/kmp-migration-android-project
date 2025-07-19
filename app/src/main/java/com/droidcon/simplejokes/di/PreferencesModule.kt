package com.droidcon.simplejokes.di

import com.droidcon.simplejokes.core.data.PreferencesDataSourceImpl
import com.droidcon.simplejokes.core.domain.datasource.PreferencesDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val preferencesModule = module {
    singleOf(::PreferencesDataSourceImpl).bind<PreferencesDataSource>()
}