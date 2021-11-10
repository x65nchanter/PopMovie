package com.example.popmovie.data

import com.example.popmovie.domain.Movie
import kotlinx.coroutines.flow.Flow

class MovieRepository(private val movieDataSource: MovieDataSource) {
    fun popularMovie(nextPage: Flow<Unit>): Flow<List<Movie>> =
        movieDataSource.allByGroup(MovieDataSource.MovieGroup.POPULAR, nextPage)
}