package com.example.popmovie.interactors

import com.example.popmovie.domain.Movie
import kotlinx.coroutines.flow.Flow

class MovieInteractors(
    val getPopularMovies: (nextPage: Flow<Unit>) -> Flow<List<Movie>>
)