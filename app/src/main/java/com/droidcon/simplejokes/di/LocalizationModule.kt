package com.droidcon.simplejokes.di

import com.droidcon.simplejokes.core.presentation.Localization
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val localizationModule = module {
    single<Localization> {
        Localization(androidApplication())
    }
}