package com.droidcon.simplejokes.di

import androidx.room.Room
import com.droidcon.simplejokes.jokes.data.database.JokesDao
import com.droidcon.simplejokes.jokes.data.database.JokesDatabase
import org.koin.dsl.module


val databaseModule = module {
    single {
        Room.databaseBuilder(
            context = get(),
            klass = JokesDatabase::class.java,
            name = JokesDatabase.DB_NAME
        )
            .build()
    }

    single<JokesDao> {
        get<JokesDatabase>().jokesDao
    }
}