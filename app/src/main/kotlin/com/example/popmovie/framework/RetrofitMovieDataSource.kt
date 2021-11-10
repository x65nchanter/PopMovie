package com.example.popmovie.framework

import com.example.popmovie.BuildConfig
import com.example.popmovie.data.MovieDataSource
import com.example.popmovie.domain.Genre
import com.example.popmovie.domain.Movie
import com.example.popmovie.domain.Poster
import com.example.popmovie.framework.services.MovieService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.fold
import java.util.*

class RetrofitMovieDataSource(
    private val movieService: MovieService
) : MovieDataSource {

    override fun allByGroup(
        group: MovieDataSource.MovieGroup,
        nextPage: Flow<Unit>
    ): Flow<List<Movie>> = when (group) {
        MovieDataSource.MovieGroup.POPULAR ->
            flow {
                val localTag = Locale.getDefault().toLanguageTag()
                val genres = movieService.genre(
                    BuildConfig.THEMOVIEDB_API_KEY,
                    localTag
                ).genres
                nextPage.fold(1) { page, _ ->
                    check(TOTAL_PAGE_RANGE.contains(page)) { "Out of page range" }
                    emit(popularMoviePage(page, localTag, genres))
                    page + 1
                }
            }.flowOn(Dispatchers.IO)
    }


    private suspend fun popularMoviePage(
        page: Int,
        localTag: String,
        genres: List<com.example.popmovie.framework.services.response.Genre>
    ) = movieService.popularMovie(
        page,
        BuildConfig.THEMOVIEDB_API_KEY,
        localTag
    ).results
        .map { moveResponse ->
            val poster =
                if (moveResponse.poster_path != null)
                    Poster.ExternalSized(
                        BuildConfig.THEMOVIEDB_IMAGE_URL,
                        "./${moveResponse.poster_path}"
                    )
                else
                    Poster.Empty

            Movie(
                moveResponse.title,
                moveResponse.overview,
                poster,
                genres.filter { moveResponse.genre_ids.contains(it.id) }
                    .map { Genre(it.name) }
            )
        }

    companion object {
        private val TOTAL_PAGE_RANGE = 1..1000
    }
}
