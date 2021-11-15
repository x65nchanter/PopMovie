package com.example.popmovie.popular.framework.response

data class Genre(
    val id: Int,
    val name: String
)

data class GenreList (
    val genres: List<Genre>
)
