package com.example.popmovie.presenter.popular

import com.airbnb.mvrx.*
import com.example.popmovie.di.AssistedViewModelFactory
import com.example.popmovie.di.hiltMavericksViewModelFactory
import com.example.popmovie.presenter.popular.PopularMoviesState.ScreenState
import com.example.popmovie.domain.Movie
import com.example.popmovie.interactors.MovieInteractors
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import ru.beryukhov.reactivenetwork.ReactiveNetwork
import javax.inject.Inject


data class PopularMoviesState(
    val screenState: ScreenState = ScreenState.IsLoading,
    val movies: WithPaging<Movie> = WithPaging(),
    val errorMessage: String? = null
) : MavericksState {
    enum class ScreenState {
        IsOffline,
        IsLoading,
        IsError,
        IsGood
    }

    data class WithPaging<T>(
        val nextAnchorIndex: Int = 0,
        val currentAnchorIndex: Int = 0,
        val buffer: List<T> = emptyList()
    ) : List<T> by buffer {
        fun isNext(): Boolean = nextAnchorIndex - currentAnchorIndex <= NEXT_FETCH_OFFSET
        fun next(page: List<T>) = copy(
            buffer = buffer + page,
            nextAnchorIndex = nextAnchorIndex + page.size
        )

        fun moveAnchor(newAnchor: Int) = copy(currentAnchorIndex = newAnchor)

        companion object {
            private const val NEXT_FETCH_OFFSET = 10
        }
    }
}

@ExperimentalCoroutinesApi
class PopularMoviesViewModel @AssistedInject constructor(
    @Assisted initialState: PopularMoviesState
) : MavericksViewModel<PopularMoviesState>(initialState) {
    @Inject
    lateinit var interactors: MovieInteractors
    init {
        ReactiveNetwork()
            .observeInternetConnectivity()
            .flowOn(Dispatchers.IO)
            .flatMapLatest { isOnline ->
                if (isOnline && ::interactors.isInitialized) {
                    interactors
                        .getPopularMovies(stateFlow.filter { it.movies.isNext() }.map {})
                        .map {
                            StateUpdateWish.MovieUpdate(it)
                        }
                } else {
                    flowOf(StateUpdateWish.IsOffline)
                }
            }
            .execute { status ->
                when (status) {
                    is Success -> wishReducer(status())
                    is Loading -> copy(screenState = ScreenState.IsLoading)
                    is Fail -> copy(
                        screenState = ScreenState.IsError,
                        //TODO: Exception mapping
                        errorMessage = status.error.localizedMessage
                    )
                    else -> copy(screenState = ScreenState.IsLoading)
                }
            }
    }

    fun movieVisibleSignal(index: Int) {
        setState {
            copy(
                screenState =
                if (movies.isNext() && screenState != ScreenState.IsOffline)
                    ScreenState.IsLoading
                else
                    screenState,
                movies = movies.moveAnchor(index)
            )
        }
    }

    private fun PopularMoviesState.wishReducer(wish: StateUpdateWish): PopularMoviesState =
        when (wish) {
            StateUpdateWish.IsOffline -> copy(screenState = ScreenState.IsOffline)
            StateUpdateWish.IsOnline -> copy(screenState = ScreenState.IsGood)
            is StateUpdateWish.MovieUpdate -> {
                copy(
                    screenState = ScreenState.IsGood,
                    movies = movies.next(wish.movies),
                )
            }
        }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<PopularMoviesViewModel, PopularMoviesState> {
        override fun create(state: PopularMoviesState): PopularMoviesViewModel
    }

    private sealed class StateUpdateWish {
        object IsOffline : StateUpdateWish()
        object IsOnline : StateUpdateWish()
        data class MovieUpdate(val movies: List<Movie>) : StateUpdateWish()
    }

    companion object :
        MavericksViewModelFactory
        <PopularMoviesViewModel, PopularMoviesState> by hiltMavericksViewModelFactory()
}
