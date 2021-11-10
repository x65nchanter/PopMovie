package com.example.popmovie.data

import com.example.popmovie.domain.Movie
import kotlinx.coroutines.flow.Flow

interface MovieDataSource {
    fun allByGroup(group: MovieGroup, nextPage: Flow<Unit>): Flow<List<Movie>>
    enum class MovieGroup {
        POPULAR
    }
}