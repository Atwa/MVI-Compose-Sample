package com.example.mvi_compose_sample.feature.movies.state

import com.example.mvi_compose_sample.feature.movies.data.Movie


data class MoviesState(
    val movies: List<Movie> = listOf(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)