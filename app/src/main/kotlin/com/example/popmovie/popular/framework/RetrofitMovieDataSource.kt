package com.example.popmovie.popular.framework

import android.util.Log
import com.example.popmovie.BuildConfig
import com.example.popmovie.data.MovieDataSource
import com.example.popmovie.domain.Genre
import com.example.popmovie.domain.Movie
import com.example.popmovie.domain.Poster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform
import java.util.*

class RetrofitMovieDataSource(
    private val theMovieDataBaseApi: TheMovieDataBaseApi
) : MovieDataSource {

    override fun allByGroup(
        group: MovieDataSource.MovieGroup
    ): Flow<Movie> = when (group) {
        MovieDataSource.MovieGroup.POPULAR ->
            TOTAL_PAGE_RANGE
                .asFlow()
                .transform { page ->
                    val localTag = Locale.getDefault().toLanguageTag()
                    val genres = theMovieDataBaseApi.genre(
                        BuildConfig.THEMOVIEDB_API_KEY,
                        localTag
                    ).genres
                    popularMoviePage(page, localTag, genres)
                        .forEach { emit(it) }
                }
                .flowOn(Dispatchers.IO)
    }

    private suspend fun popularMoviePage(
        page: Int,
        localTag: String,
        genres: List<com.example.popmovie.popular.framework.response.Genre>
    ): List<Movie> = theMovieDataBaseApi.popularMovie(
        page,
        BuildConfig.THEMOVIEDB_API_KEY,
        localTag
    )
        .results
        .map { moveResponse ->
            val poster =
                if (moveResponse.poster_path != null) Poster.ExternalSized(
                    BuildConfig.THEMOVIEDB_IMAGE_URL,
                    "./${moveResponse.poster_path}"
                )
                else Poster.Empty

            Movie(
                moveResponse.title,
                moveResponse.overview,
                poster,
                genres.filter { moveResponse.genre_ids.contains(it.id) }
                    .map { Genre(it.name) }
            )
        }.also { Log.d(this::class.java.canonicalName, " Page request: $page") }

    companion object {
        private val TOTAL_PAGE_RANGE = 1..1000
    }
}
