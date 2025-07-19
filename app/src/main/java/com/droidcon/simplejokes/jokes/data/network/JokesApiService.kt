package com.droidcon.simplejokes.jokes.data.network

import com.droidcon.simplejokes.jokes.data.model.JokeDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class JokesApiService(private val client: HttpClient) {
    suspend fun getRandomJokes(): List<JokeDto> {
        return client.get("random_ten").body()
    }
}