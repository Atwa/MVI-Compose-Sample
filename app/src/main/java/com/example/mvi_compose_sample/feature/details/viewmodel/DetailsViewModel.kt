package com.example.mvi_compose_sample.feature.details.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.mvi_compose_sample.utils.BaseViewModel
import com.example.mvi_compose_sample.feature.details.intent.DetailsIntent
import com.example.mvi_compose_sample.feature.details.state.DetailsState
import com.example.mvi_compose_sample.feature.movies.data.IMovieRepo
import com.example.mvi_compose_sample.feature.movies.data.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: IMovieRepo) :
    BaseViewModel<DetailsState, DetailsIntent>() {

    lateinit var movie: Movie

    override fun getInitialState(): DetailsState = DetailsState(movie = movie)

    override fun processIntents(intent: DetailsIntent) {
        when (intent) {
            is DetailsIntent.FetchTrailers -> fetchMovieTrailers()
            is DetailsIntent.GetLikeState -> getLikeState()
            is DetailsIntent.UpdateLikeState -> updateLikeStatus()
        }
    }

    private fun fetchMovieTrailers() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                updateState { oldState -> oldState.copy(isLoading = true) }
                val results = repository.fetchMovieTrailers(movie.id).body()?.results
                updateState { oldState ->
                    oldState.copy(
                        isLoading = false,
                        trailerExternalIntent = null,
                        trailers = results
                    )
                }
            } catch (e: Exception) {
                updateState { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    private fun updateLikeStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            val newLikeState = repository.isMovieLiked(movie.id).not()
            repository.changeLikeState(movie, newLikeState)
            withContext(Dispatchers.Main) {
                updateState { it.copy(trailerExternalIntent = null, isLiked = newLikeState) }
            }
        }
    }

    private fun getLikeState() {
        viewModelScope.launch(Dispatchers.IO) {
            val likeState = repository.isMovieLiked(movie.id)
            withContext(Dispatchers.Main) {
                updateState { it.copy(isLiked = likeState) }
            }
        }
    }


}