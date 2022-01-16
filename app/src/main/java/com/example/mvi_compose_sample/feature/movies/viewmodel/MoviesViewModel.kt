package com.example.mvi_compose_sample.feature.movies.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.mvi_compose_sample.utils.BaseViewModel
import com.example.mvi_compose_sample.feature.movies.data.IMovieRepo
import com.example.mvi_compose_sample.feature.movies.intent.MoviesIntent
import com.example.mvi_compose_sample.feature.movies.state.MoviesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(private val repository: IMovieRepo) :
    BaseViewModel<MoviesState, MoviesIntent>() {

    override fun getInitialState(): MoviesState = MoviesState()

    override fun processIntents(intent: MoviesIntent) {
        when (intent) {
            MoviesIntent.FetchMovies -> fetchMovies()
        }
    }

    private fun fetchMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                updateState { oldState -> oldState.copy(isLoading = true) }
                repository.getPopularMovies().body()?.results?.let {
                    updateState { oldState ->
                        oldState.copy(
                            isLoading = false,
                            movies = it
                        )
                    }
                }
            } catch (e: Exception) {
                updateState { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }


}