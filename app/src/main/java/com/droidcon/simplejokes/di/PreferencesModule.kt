package com.droidcon.simplejokes.di

import com.droidcon.simplejokes.core.data.PreferencesDataSourceImpl
import com.droidcon.simplejokes.core.domain.datasource.PreferencesDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesModule {

    @Binds
    abstract fun bindPreferencesDataSource(
        impl: PreferencesDataSourceImpl
    ): PreferencesDataSource
}