package com.example.popmovie.popular.ui

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.example.popmovie.popular.ui.PopularMoviesViewModel
import kotlinx.coroutines.flow.collect


@Composable
fun PopularMovieScreen() {
    val viewModel: PopularMoviesViewModel = mavericksViewModel()
    val isOnline by viewModel.collectAsState { !it.isOffline }
    val isLoading by viewModel.collectAsState { it.isLoading }
    val errorMessage by viewModel.collectAsState { it.errorMessage }
    val movies by viewModel.collectAsState { it.movies }
    val pageState: LazyListState = rememberLazyListState()

    PopularMovieContent(isOnline, isLoading, errorMessage, movies, pageState)

    LaunchedEffect(pageState) {
        snapshotFlow { pageState.firstVisibleItemIndex }
            .collect {
                viewModel.onMovieVisible(it)
            }
    }
}
