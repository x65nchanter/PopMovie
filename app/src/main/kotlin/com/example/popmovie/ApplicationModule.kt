package com.example.popmovie

import com.example.popmovie.framework.RetrofitMovieDataSource
import com.example.popmovie.framework.services.MovieService
import com.example.popmovie.data.MovieRepository
import com.example.popmovie.interactors.MovieInteractors
import com.example.popmovie.interactors.getPopularMovie
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    @Provides
    fun retrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.THEMOVIEDB_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun moviesService(retrofit: Retrofit): MovieService =
        retrofit.create(MovieService::class.java)

    @Inject
    @Provides
    fun movieRepository(
        movieService: MovieService
    ): MovieRepository =
        MovieRepository(RetrofitMovieDataSource(movieService))

    @Provides
    @Inject
    fun movieInteractors(movieRepository: MovieRepository) =
        MovieInteractors(movieRepository::getPopularMovie)
}
