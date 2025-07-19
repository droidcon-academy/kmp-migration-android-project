package com.droidcon.simplejokes.jokes.presentation.screens.jokes_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.droidcon.simplejokes.R
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JokesListScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: JokesListViewModel = koinViewModel(),
    onGoToJokeDetails: (Int) -> Unit,
    onOpenPreferences: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.eventChannel.collect {
            when (it) {
                is JokesListEvent.GotoJokeDetails -> {
                    onGoToJokeDetails(it.jokeId)
                }

                is JokesListEvent.ShowError -> {
                    viewModel.snackbarMessage(it.message)
                }
            }
        }
    }

    var menuExpanded by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        modifier = modifier,
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.jokes_list_title)) },
                actions = {
                    // Add IconButton for the menu
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "content description"
                        )
                    }
                    // Add DropdownMenu
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.preferences_title)) },
                            onClick = {
                                menuExpanded = false
                                onOpenPreferences()
                            }
                        )
                    }
                }
            )
        },
    ) { scaffoldInnerPadding ->
        JokesListScreen(
            modifier = Modifier.padding(scaffoldInnerPadding),
            state = state,
            onIntent = viewModel::onIntent
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun JokesListScreen(
    modifier: Modifier = Modifier,
    state: JokesListState,
    onIntent: (JokesListIntent) -> Unit
) {
    // Add tab state
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(R.string.tab_all_jokes),
        stringResource(R.string.tab_favorites)
    )

    val pullToRefreshState = rememberPullToRefreshState()
    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
        state = pullToRefreshState,
        isRefreshing = state.refreshing,
        onRefresh = { onIntent(JokesListIntent.Refresh) },
        indicator = {
            PullToRefreshDefaults.Indicator(
                state = pullToRefreshState,
                isRefreshing = state.refreshing,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            // Add TabRow
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }
            // Filter jokes based on selected tab
            val displayedJokes = when (selectedTabIndex) {
                0 -> state.jokes // All jokes
                1 -> state.jokes.filter { it.isFavorite } // Only favorites
                else -> state.jokes
            }

            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
            ) {
                items(
                    items = displayedJokes,
                    key = { it.id }
                ) { joke ->
                    Card(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onIntent(JokesListIntent.ClickOnJoke(joke.id))
                                }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = joke.question, fontWeight = FontWeight.Bold)
                                Text(text = joke.answer)
                            }

                            // Favorite icon
                            IconButton(
                                onClick = { onIntent(JokesListIntent.ToggleFavorite(joke)) },
                                modifier = Modifier.size(40.dp)
                            ) {
                                if (joke.isFavorite) {
                                    Icon(
                                        imageVector = Icons.Filled.Favorite,
                                        contentDescription = "Remove from favorites",
                                        tint = Color.Red
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Outlined.FavoriteBorder,
                                        contentDescription = "Add to favorites",
                                        tint = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (state.loading) {
            CircularProgressIndicator()
        }
    }
}