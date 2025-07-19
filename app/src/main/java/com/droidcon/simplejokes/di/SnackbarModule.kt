package com.droidcon.simplejokes.di

import com.droidcon.simplejokes.core.presentation.SnackbarManager
import org.koin.dsl.module

val snackbarModule = module {
    single { SnackbarManager() }
}