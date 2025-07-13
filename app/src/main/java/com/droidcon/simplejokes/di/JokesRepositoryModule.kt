package com.droidcon.simplejokes.di

import com.droidcon.simplejokes.jokes.data.JokesRepositoryImpl
import com.droidcon.simplejokes.jokes.domain.JokesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class JokesRepositoryModule {

    @Binds
    abstract fun bindJokesRepository(
        jokesRepositoryImpl: JokesRepositoryImpl
    ): JokesRepository
}