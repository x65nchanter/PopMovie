package com.example.popmovie.domain


data class Movie(
    val title: String,
    val overview: String,
    val poster: Poster,
    val genres: List<Genre>
)
