package com.example.popmovie.interactors

import com.example.popmovie.data.MovieRepository
import com.example.popmovie.domain.Movie
import kotlinx.coroutines.flow.Flow

fun MovieRepository.getPopularMovie(nextPage: Flow<Unit>): Flow<List<Movie>> =
    popularMovie(nextPage)
