package com.droidcon.simplejokes.di

import com.droidcon.simplejokes.jokes.data.JokesRepositoryImpl
import com.droidcon.simplejokes.jokes.domain.JokesRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val jokesRepositoryModule = module {
    singleOf(::JokesRepositoryImpl).bind<JokesRepository>()
}