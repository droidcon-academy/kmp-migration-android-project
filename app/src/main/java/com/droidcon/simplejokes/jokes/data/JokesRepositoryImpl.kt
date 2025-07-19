package com.droidcon.simplejokes.jokes.data

import co.touchlab.kermit.Logger
import com.droidcon.simplejokes.jokes.data.database.JokesDao
import com.droidcon.simplejokes.jokes.data.mappers.toEntity
import com.droidcon.simplejokes.jokes.data.mappers.toJoke
import com.droidcon.simplejokes.jokes.data.network.JokesApiService
import com.droidcon.simplejokes.jokes.domain.JokesRepository
import com.droidcon.simplejokes.jokes.domain.model.Joke
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Repository implementation for jokes data with offline-first approach.
 */
class JokesRepositoryImpl(
    private val apiService: JokesApiService,
    private val database: JokesDao
) : JokesRepository {
    /**
     * Gets a Flow of all jokes from the database.
     */
    override fun getJokes(): Flow<List<Joke>> {
        return database.getAllJokes().map { entities ->
            entities.map { it.toJoke() }
        }
    }

    /**
     * Fetches jokes from the network API and updates the database.
     */
    @OptIn(ExperimentalTime::class)
    override suspend fun fetchJokesFromApi(): Result<Unit> {
        return runCatching {
            val startTime = Clock.System.now()

            // Get favorite jokes IDs to preserve them
            val favoriteJokesIds = database.getFavoriteJokesIds()

            // Fetch jokes from the API using Retrofit
            val remoteJokes = apiService.getRandomJokes()

            val jokesToUpsert = remoteJokes
                .filter { jokeDto ->
                    jokeDto.id !in favoriteJokesIds
                }
                .map { jokeDto ->
                    jokeDto.toJoke().toEntity()
                }

            // Delete only non-favorite jokes
            database.deleteAllNonFavoriteJokes()

            // Upsert new jokes
            database.upsertJokes(jokesToUpsert)

            val endTime = Clock.System.now()
            val elapsedTime = endTime - startTime

            Logger.withTag("JokesRepository").d("Successfully updated database with ${remoteJokes.size} jokes in $elapsedTime ms")
        }
    }

    // Rest of the methods remain the same
    override suspend fun getJokeById(id: Int): Result<Joke?> {
        return runCatching {
            database.getJokeById(id)?.toJoke()
        }
    }

    override suspend fun toggleFavorite(jokeId: Int): Result<Unit> {
        return runCatching {
            database.toggleFavorite(jokeId)
        }
    }

    override suspend fun setFavorite(jokeId: Int, isFavorite: Boolean): Result<Unit> {
        return runCatching {
            database.setFavorite(jokeId, isFavorite)
        }
    }

    override suspend fun isFavorite(jokeId: Int): Result<Boolean> {
        return runCatching {
            database.getJokeById(jokeId)?.isFavorite ?: false
        }
    }
}