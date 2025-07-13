package com.droidcon.simplejokes.jokes.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.droidcon.simplejokes.jokes.data.model.JokeEntity

@Database(entities = [JokeEntity::class], version = 1, exportSchema = false)
abstract class JokesDatabase : RoomDatabase() {
    abstract val jokesDao: JokesDao

    companion object {
        const val DB_NAME = "jokes.db"
    }
}