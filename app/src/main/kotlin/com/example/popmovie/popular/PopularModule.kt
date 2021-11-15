package com.example.popmovie.popular

import com.example.popmovie.BuildConfig
import com.example.popmovie.data.MovieRepository
import com.example.popmovie.di.AssistedViewModelFactory
import com.example.popmovie.di.ViewModelKey
import com.example.popmovie.interactors.GetPopularMovie
import com.example.popmovie.interactors.MovieInteractors
import com.example.popmovie.popular.framework.TheMovieDataBaseApi
import com.example.popmovie.popular.framework.RetrofitMovieDataSource
import com.example.popmovie.popular.ui.PopularMoviesViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class PopularModule {
    @Provides
    fun dependencyLayer(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.THEMOVIEDB_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun frameworkLayer(retrofit: Retrofit): TheMovieDataBaseApi =
        retrofit.create(TheMovieDataBaseApi::class.java)

    @Inject
    @Provides
    fun dataLayer(
        theMovieDataBaseApi: TheMovieDataBaseApi
    ): MovieRepository =
        MovieRepository(RetrofitMovieDataSource(theMovieDataBaseApi))

    @Provides
    @Inject
    fun useCaseLayer(movieRepository: MovieRepository): MovieInteractors =
        MovieInteractors(GetPopularMovie(movieRepository))

    @Provides
    @IntoMap
    @ViewModelKey(PopularMoviesViewModel::class)
    fun presentationLayer(factory: PopularMoviesViewModel.Factory):
            AssistedViewModelFactory<*, *> = factory
}
