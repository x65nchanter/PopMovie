package com.example.popmovie.popular.ui

import com.airbnb.mvrx.MavericksState
import com.example.popmovie.domain.Movie

data class PopularMoviesState(
    val isLoading: Boolean = true,
    val isOffline: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val currentVisible: Int = -1,
    val errorMessage: String? = null
) : MavericksState