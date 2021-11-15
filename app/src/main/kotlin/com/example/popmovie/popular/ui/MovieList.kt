package com.example.popmovie.popular.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.example.popmovie.domain.Movie


@Composable
fun MoviesList(movies: List<Movie>, listState: LazyListState) {
    LazyColumn(state = listState) {
        items(movies) { movie ->
            MovieItem(movie = movie)
        }
    }
}
