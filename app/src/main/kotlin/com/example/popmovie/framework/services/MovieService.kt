package com.example.popmovie.framework.services

import com.example.popmovie.framework.services.response.GenreList
import com.example.popmovie.framework.services.response.MoviePage
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    @GET("movie/popular")
    suspend fun popularMovie(
        @Query("page") page: Int,
        //TODO: нормальную авторизацию на основе токена
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ) : MoviePage

    @GET("genre/movie/list")
    suspend fun genre(
        @Query("api_key") api_key: String,
        @Query("language") language: String
    ): GenreList
}
