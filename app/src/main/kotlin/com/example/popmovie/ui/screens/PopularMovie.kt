package com.example.popmovie.ui.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.example.popmovie.R
import com.example.popmovie.presenter.popular.PopularMoviesState.ScreenState
import com.example.popmovie.presenter.popular.PopularMoviesViewModel
import com.example.popmovie.ui.collection.MoviesList
import com.example.popmovie.ui.states.ErrorState
import com.example.popmovie.ui.states.LoadingState
import com.example.popmovie.ui.states.OfflineState
import com.example.popmovie.ui.theme.PopMovieTheme
import com.example.popmovie.domain.Genre
import com.example.popmovie.domain.Movie
import com.example.popmovie.domain.Poster
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect


@ExperimentalCoroutinesApi
@ExperimentalAnimationApi
@Composable
fun PopularMovie() {
    val viewModel: PopularMoviesViewModel = mavericksViewModel()
    val screenState by viewModel.collectAsState { it.screenState }
    val errorMessage by viewModel.collectAsState { it.errorMessage }
    val movies by viewModel.collectAsState { it.movies }
    val pageState: LazyListState = rememberLazyListState()

    PopularMovieContent(screenState, errorMessage, movies, pageState)

    LaunchedEffect(pageState) {
        snapshotFlow { pageState.firstVisibleItemIndex }
            .collect {
                viewModel.movieVisibleSignal(it)
            }
    }
}

@ExperimentalAnimationApi
@Composable
fun PopularMovieContent(
    screenState: ScreenState,
    errorMessage: String?,
    movies: List<Movie>,
    pageState: LazyListState
) {
    when {
        screenState == ScreenState.IsLoading && movies.isNotEmpty() ->
            Column(Modifier.fillMaxSize()) {
                LoadingState()
                MoviesList(movies = movies, pageState)
            }
        screenState == ScreenState.IsLoading -> LoadingState()
        screenState == ScreenState.IsGood -> MoviesList(movies = movies, pageState)
        screenState == ScreenState.IsOffline -> OfflineState()
        screenState == ScreenState.IsError -> ErrorState(
            message = errorMessage ?: stringResource(id = R.string.unexpected_error)
        )
    }
}

@ExperimentalAnimationApi
@Preview(
    name = "LoadingState",
    showBackground = true
)
@Composable
fun LoadingPreview() {
    PopMovieTheme {
        PopularMovieContent(
            ScreenState.IsLoading,
            null,
            emptyList(),
            rememberLazyListState()
        )
    }
}

@ExperimentalAnimationApi
@Preview(
    name = "ErrorPreview",
    showBackground = true
)
@Composable
fun ErrorPreview() {
    PopMovieTheme {
        PopularMovieContent(
            ScreenState.IsError,
            "Some error message",
            emptyList(),
            rememberLazyListState()
        )
    }
}

@ExperimentalAnimationApi
@Preview(
    name = "OfflinePreview",
    showBackground = true
)
@Composable
fun OfflinePreview() {
    PopMovieTheme {
        PopularMovieContent(
            ScreenState.IsOffline,
            null,
            emptyList(),
            rememberLazyListState()
        )
    }
}

@ExperimentalStdlibApi
@ExperimentalAnimationApi
@Preview(
    name = "MovieListPreview",
    showBackground = true
)
@Composable
fun MovieListPreview() {
    val moviesExamples = buildList {
        add(
            Movie("Movie Title", "Movie Overview", Poster.Empty, listOf(
                "Genre", "Genre 1", "Genre 2", "Genre 3"
            ).map { Genre((it)) })
        )
    }
    PopMovieTheme {
        PopularMovieContent(
            ScreenState.IsGood,
            null,
            moviesExamples,
            rememberLazyListState()
        )
    }
}