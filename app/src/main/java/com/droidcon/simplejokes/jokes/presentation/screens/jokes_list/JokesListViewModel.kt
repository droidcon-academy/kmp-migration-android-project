package com.droidcon.simplejokes.jokes.presentation.screens.jokes_list

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidcon.simplejokes.core.presentation.SnackbarManager
import com.droidcon.simplejokes.jokes.domain.JokesRepository
import com.droidcon.simplejokes.jokes.domain.model.Joke
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Immutable
data class JokesListState(
    val jokes: List<Joke> = emptyList(),
    val loading: Boolean = false,
    val refreshing: Boolean = false
)

sealed interface JokesListIntent {
    data object FirstLoad: JokesListIntent
    data object Refresh: JokesListIntent
    data class ClickOnJoke(val jokeId: Int): JokesListIntent
    data class ToggleFavorite(val joke: Joke): JokesListIntent
}

sealed interface JokesListEvent {
    data class GotoJokeDetails(val jokeId: Int): JokesListEvent
    data class ShowError(val message: String): JokesListEvent
}

/**
 * ViewModel for the jokes list screen with manual database observation and network loading.
 */
class JokesListViewModel(
    private val repository: JokesRepository,
    private val snackbarManager: SnackbarManager
    ): ViewModel() {

    // The rest of the implementation remains the same
    private val _state = MutableStateFlow(JokesListState())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<JokesListEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    init {
        loadFromDB()
        loadFromNetwork(viaRefresh = false)
    }

    fun onIntent(intent: JokesListIntent) {
        when(intent) {
            is JokesListIntent.FirstLoad -> {
                loadFromNetwork(viaRefresh = false)
            }

            is JokesListIntent.Refresh -> {
                loadFromNetwork(viaRefresh = true)
            }

            is JokesListIntent.ClickOnJoke -> {
                viewModelScope.launch {
                    _eventChannel.send(JokesListEvent.GotoJokeDetails(intent.jokeId))
                }
            }

            is JokesListIntent.ToggleFavorite -> {
                toggleFavorite(intent.joke)
            }
        }
    }

    /**
     * Toggle the favorite status of a joke
     */
    private fun toggleFavorite(joke: Joke) {
        viewModelScope.launch {
            repository.toggleFavorite(joke.id)
                .onFailure { error ->
                    _eventChannel.send(JokesListEvent.ShowError("Failed to update favorite status: ${error.message}"))
                }
        }
    }

    /**
     * Sets up observation of the local database.
     * This starts immediately to show cached data.
     */
    private fun loadFromDB() {
        viewModelScope.launch {
            // Start collecting from the Flow of jokes from the database
            repository.getJokes().collect { jokes ->
                _state.value = _state.value.copy(
                    jokes = jokes
                )
            }
        }
    }

    private fun setLoading(status: Boolean, viaRefresh: Boolean = false) {
        if (viaRefresh) {
            _state.update { it.copy(refreshing = status) }
        } else {
            _state.update { it.copy(loading = status) }
        }
    }

    /**
     * Loads fresh data from the network API.
     * This doesn't directly update the UI state, but triggers
     * database updates which will be observed via loadFromDB().
     */
    private fun loadFromNetwork(viaRefresh: Boolean) {
        viewModelScope.launch {
            setLoading(true, viaRefresh)

            repository.fetchJokesFromApi()
                .onSuccess {
                    // Success is handled through database Flow updates
                    setLoading(false, viaRefresh)
                }
                .onFailure { error ->
                    val errorMessage = error.message ?: "Network error. Using cached data."
                    _eventChannel.send(JokesListEvent.ShowError(errorMessage))
                    setLoading(false, viaRefresh)
                }
        }
    }

    suspend fun snackbarMessage(message: String) {
        snackbarManager.showMessage(message)
    }
}