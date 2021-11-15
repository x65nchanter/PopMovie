package com.example.popmovie.interactors

import com.example.popmovie.data.MovieRepository
import com.example.popmovie.domain.Movie
import kotlinx.coroutines.flow.Flow

class GetPopularMovie(private val movieRepository: MovieRepository) {
    fun execute(): Flow<Movie> =
        movieRepository.popularMovie()
}
