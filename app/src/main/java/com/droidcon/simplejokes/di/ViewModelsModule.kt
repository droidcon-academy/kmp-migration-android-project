package com.droidcon.simplejokes.di

import com.droidcon.simplejokes.jokes.presentation.screens.joke_details.JokesDetailsViewModel
import com.droidcon.simplejokes.jokes.presentation.screens.jokes_list.JokesListViewModel
import com.droidcon.simplejokes.jokes.presentation.screens.preferences.PreferencesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelsModule = module {
    viewModelOf(::JokesListViewModel)
    viewModelOf(::JokesDetailsViewModel)
    viewModelOf(::PreferencesViewModel)
}