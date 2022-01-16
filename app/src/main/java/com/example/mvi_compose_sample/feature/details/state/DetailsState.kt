package com.example.mvi_compose_sample.feature.details.state

import android.content.Intent
import com.example.mvi_compose_sample.feature.details.data.Trailer
import com.example.mvi_compose_sample.feature.movies.data.Movie

data class DetailsState(
    val movie: Movie? = null,
    val isLoading: Boolean = false,
    val trailers: List<Trailer>? = null,
    val isLiked: Boolean = false,
    val trailerExternalIntent : Intent? = null,
    val errorMessage: String? = null
)