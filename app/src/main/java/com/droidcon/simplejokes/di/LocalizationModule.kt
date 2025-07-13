package com.droidcon.simplejokes.di

import android.content.Context
import com.droidcon.simplejokes.core.presentation.Localization
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalizationModule {

    @Provides
    @Singleton
    fun provideLocalization(@ApplicationContext context: Context): Localization {
        return Localization(context)
    }
}