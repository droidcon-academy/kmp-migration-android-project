package com.droidcon.simplejokes.jokes.presentation.screens.joke_details

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidcon.simplejokes.jokes.domain.JokesRepository
import com.droidcon.simplejokes.jokes.domain.model.Joke
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Immutable
data class JokeDetailsState(
    val joke: Joke? = null,
    val loading: Boolean = false
)

sealed interface JokeDetailsIntent {
    data object ToggleFavorite: JokeDetailsIntent
}

sealed interface JokesDetailsEvent {
    data object GoBack: JokesDetailsEvent
}

class JokesDetailsViewModel(
    private val repository: JokesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(JokeDetailsState(loading = true))
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<JokesDetailsEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    init {
        print("JokeDetailsViewModel init")
        val jokeId = savedStateHandle.get<Int>("jokeId") ?: 0
        print("JokeId: $jokeId")
        loadJoke(jokeId)
    }

    private fun loadJoke(jokeId: Int) {

        viewModelScope.launch {
            repository.getJokeById(jokeId)
                .onSuccess { joke ->
                    joke?.let { joke ->
                        print("Joke: $joke")
                        _state.update { it.copy(joke = joke, loading = false) }
                    } ?: run {
                        print("Joke is null")
                        _state.update { it.copy(loading = false) }
                    }
                }
                .onFailure { error ->
                    print("Error: $error")
                    _state.update { it.copy(loading = false) }
                }
        }
    }

    fun onIntent(intent: JokeDetailsIntent) {
        when(intent) {
            JokeDetailsIntent.ToggleFavorite -> {
                toggleFavorite()
            }
        }
    }

    private fun toggleFavorite() {
        viewModelScope.launch {
            val jokeId = state.value.joke?.id ?: return@launch

            repository.toggleFavorite(jokeId)
                .onSuccess {
                    // Reload the joke to reflect the change
                    loadJoke(jokeId)
                }
        }
    }
}