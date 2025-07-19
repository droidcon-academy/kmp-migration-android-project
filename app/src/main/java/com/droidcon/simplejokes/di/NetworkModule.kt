package com.droidcon.simplejokes.di

import com.droidcon.simplejokes.jokes.data.network.JokesApiService
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module


val networkModule = module {
    single<HttpClientEngine> {
        OkHttp.create()
    }

    single<HttpClient> {
        HttpClient(engine = get()) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true // Be resilient to new fields in the JSON
                    prettyPrint = true       // Useful for logging
                    isLenient = true         // Be lenient to no-compliant JSON features

                })
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        co.touchlab.kermit.Logger.d(message)
                    }
                }
                level = LogLevel.ALL
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
                url {
                    takeFrom("https://official-joke-api.appspot.com")
                }
            }
        }
    }
    single { JokesApiService(get()) }
}