package com.example.popmovie.popular.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.popmovie.domain.Genre
import com.example.popmovie.domain.Movie
import com.example.popmovie.domain.Poster
import com.example.popmovie.ui.states.ErrorState
import com.example.popmovie.ui.states.LoadingState
import com.example.popmovie.ui.states.OfflineState
import com.example.popmovie.ui.theme.PopMovieTheme

@Composable
fun PopularMovieContent(
    isOnline: Boolean,
    isLoading: Boolean,
    errorMessage: String?,
    movies: List<Movie>,
    pageState: LazyListState
) {
    when {
        errorMessage != null -> ErrorState(
            message = errorMessage
        )
        isOnline && isLoading ->
            Column(Modifier.fillMaxSize()) {
                LoadingState()
                MoviesList(movies = movies, pageState)
            }
        !isOnline && isLoading ->
            Column(Modifier.fillMaxSize()) {
                LoadingState()
                OfflineState()
            }
        isOnline -> MoviesList(movies = movies, pageState)
        !isOnline -> OfflineState()
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
            isLoading = true,
            isOnline = true,
            errorMessage = null,
            movies = emptyList(),
            pageState = rememberLazyListState()
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
            isLoading = false,
            isOnline = true,
            errorMessage = "Some error message",
            movies = emptyList(),
            pageState = rememberLazyListState()
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
            isLoading = false,
            isOnline = false,
            errorMessage = null,
            movies = emptyList(),
            pageState = rememberLazyListState()
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
            isLoading = false,
            isOnline = true,
            errorMessage = null,
            movies = moviesExamples,
            pageState = rememberLazyListState()
        )
    }
}