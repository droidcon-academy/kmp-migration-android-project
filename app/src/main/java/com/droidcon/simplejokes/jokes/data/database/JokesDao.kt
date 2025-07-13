package com.droidcon.simplejokes.jokes.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.droidcon.simplejokes.jokes.data.model.JokeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JokesDao {
    /**
     * Get all jokes ordered by favorite status (favorites first) and then by ID
     */
    // @Query("SELECT * FROM joke ORDER BY isFavorite DESC, id ASC")
    @Query("SELECT * FROM joke")
    fun getAllJokes(): Flow<List<JokeEntity>>

    /**
     * Get only favorite jokes
     */
    @Query("SELECT * FROM joke WHERE isFavorite = 1")
    suspend fun getFavoriteJokes(): List<JokeEntity>

    /**
     * Update or insert jokes
     */
    @Upsert
    suspend fun upsertJokes(jokes: List<JokeEntity>)

    /**
     * Update or insert a single joke
     */
    @Upsert
    suspend fun upsertJoke(joke: JokeEntity)

    /**
     * Delete all non-favorite jokes
     */
    @Query("DELETE FROM joke WHERE isFavorite = 0")
    suspend fun deleteAllNonFavoriteJokes()

    /**
     * Delete all jokes (including favorites)
     */
    @Query("DELETE FROM joke")
    suspend fun deleteAllJokes()

    /**
     * Get a joke by ID
     */
    @Query("SELECT * FROM joke WHERE id = :jokeId")
    suspend fun getJokeById(jokeId: Int): JokeEntity?

    /**
     * Toggle favorite status for a joke
     */
    @Query("UPDATE joke SET isFavorite = NOT isFavorite WHERE id = :jokeId")
    suspend fun toggleFavorite(jokeId: Int)

    /**
     * Set favorite status for a joke
     */
    @Query("UPDATE joke SET isFavorite = :isFavorite WHERE id = :jokeId")
    suspend fun setFavorite(jokeId: Int, isFavorite: Boolean)

    @Query("SELECT id FROM joke WHERE isFavorite = 1")
    suspend fun getFavoriteJokesIds(): List<Int>
}