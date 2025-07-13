package com.droidcon.simplejokes.jokes.data.network

import com.droidcon.simplejokes.jokes.data.model.JokeDto
import retrofit2.http.GET

interface JokesApiService {
    @GET("/random_ten")
    suspend fun getRandomJokes(): List<JokeDto>
}