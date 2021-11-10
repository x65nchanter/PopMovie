package com.example.popmovie.presenter

import com.example.popmovie.di.AssistedViewModelFactory
import com.example.popmovie.di.MavericksViewModelComponent
import com.example.popmovie.di.ViewModelKey
import com.example.popmovie.presenter.popular.PopularMoviesViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Module
@InstallIn(MavericksViewModelComponent::class)
interface PresenterModule {
    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(PopularMoviesViewModel::class)
    fun popularMoviesModelFactory(factory: PopularMoviesViewModel.Factory):
            AssistedViewModelFactory<*, *>
}
