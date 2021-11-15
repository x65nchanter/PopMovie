package com.example.popmovie.popular.ui

import com.airbnb.mvrx.*
import com.example.popmovie.di.AssistedViewModelFactory
import com.example.popmovie.di.hiltMavericksViewModelFactory
import com.example.popmovie.domain.Movie
import com.example.popmovie.interactors.MovieInteractors
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import ru.beryukhov.reactivenetwork.ReactiveNetwork


class PopularMoviesViewModel @AssistedInject constructor(
    interactors: MovieInteractors,
    @Assisted initialState: PopularMoviesState
) : MavericksViewModel<PopularMoviesState>(initialState) {
    init {
        internetConnectivity()
            .combineTransform(interactors.nextMovie()) { networkEffect, nextMovieEffect ->
                emit(networkEffect)
                if (networkEffect == StateEffect.IsOnline) {
                    emit(nextMovieEffect)
                }
            }
            .execute { status ->
                when (status) {
                    is Success -> status().run { reduce() }
                    is Loading -> copy(isLoading = true)
                    is Fail -> copy(
                        errorMessage = status.error.localizedMessage
                    )
                    else -> this
                }
            }
    }

    fun onMovieVisible(index: Int) {
        setState {
            copy(
                isLoading = isNextMovie(),
                currentVisible = index
            )
        }
    }

    private fun MovieInteractors.nextMovie() = getPopularMovies
        .execute()
        .zip(stateFlow.filter { it.isNextMovie() }) { next, _ ->
            StateEffect.NextMovie(next)
        }

    private fun PopularMoviesState.isNextMovie() =
        currentVisible >= (movies.size - MOVIE_PREFETCH_COUNT) && !isOffline && !isLoading

    private fun internetConnectivity() = ReactiveNetwork()
        .observeInternetConnectivity()
        .flowOn(Dispatchers.IO)
        .map { isOnline ->
            if (isOnline) {
                StateEffect.IsOnline
            } else {
                StateEffect.IsOffline
            }
        }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<PopularMoviesViewModel, PopularMoviesState> {
        override fun create(state: PopularMoviesState): PopularMoviesViewModel
    }

    private sealed class StateEffect {
        object IsOffline : StateEffect()
        object IsOnline : StateEffect()
        data class NextMovie(val movie: Movie) : StateEffect()

        fun PopularMoviesState.reduce(): PopularMoviesState =
            when {
                this@StateEffect == IsOffline ->
                    copy(isOffline = true)
                this@StateEffect == IsOnline ->
                    copy(isOffline = false)
                this@StateEffect is NextMovie && !movies.contains(movie) -> {
                    copy(
                        isLoading = false,
                        movies = movies + movie,
                    )
                }
                else -> this
            }
    }

    companion object :
        MavericksViewModelFactory
        <PopularMoviesViewModel, PopularMoviesState> by hiltMavericksViewModelFactory() {
        private const val MOVIE_PREFETCH_COUNT = 10
    }
}
