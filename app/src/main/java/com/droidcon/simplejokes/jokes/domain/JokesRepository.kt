package com.droidcon.simplejokes.jokes.domain

import com.droidcon.simplejokes.jokes.domain.model.Joke
import kotlinx.coroutines.flow.Flow

interface JokesRepository {
    /**
     * Gets a Flow of all jokes from the database.
     */
    fun getJokes(): Flow<List<Joke>>

    /**
     * Fetches jokes from the network API and updates the database.
     */
    suspend fun fetchJokesFromApi(): Result<Unit>

    // Rest of the methods remain the same
    suspend fun getJokeById(id: Int): Result<Joke?>

    suspend fun toggleFavorite(jokeId: Int): Result<Unit>

    suspend fun setFavorite(jokeId: Int, isFavorite: Boolean): Result<Unit>

    suspend fun isFavorite(jokeId: Int): Result<Boolean>
}