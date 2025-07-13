package com.droidcon.simplejokes

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.droidcon.simplejokes.jokes.presentation.screens.joke_details.JokeDetailsScreenRoot
import com.droidcon.simplejokes.jokes.presentation.screens.jokes_list.JokesListScreenRoot
import com.droidcon.simplejokes.jokes.presentation.screens.preferences.PreferencesScreenRoot
import kotlinx.serialization.Serializable

sealed interface RandomJokesRoute {
    @Serializable data object JokesList: RandomJokesRoute
    @Serializable data class SelectedJoke(val jokeId: Int): RandomJokesRoute
    @Serializable data object Preferences: RandomJokesRoute
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = RandomJokesRoute.JokesList,
    ) {
        composable<RandomJokesRoute.JokesList> {
            JokesListScreenRoot(
                modifier = modifier,
                onGoToJokeDetails = { jokeId ->
                    navController.navigate(RandomJokesRoute.SelectedJoke(jokeId))
                },
                onOpenPreferences = {
                    navController.navigate(RandomJokesRoute.Preferences)
                }
            )
        }

        composable<RandomJokesRoute.SelectedJoke>() {
            JokeDetailsScreenRoot(
                modifier = modifier,
                onGoBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<RandomJokesRoute.Preferences>() {
            PreferencesScreenRoot(
                onGoBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}