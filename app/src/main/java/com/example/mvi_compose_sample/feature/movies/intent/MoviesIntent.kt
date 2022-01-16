package com.example.mvi_compose_sample.feature.movies.intent

sealed class MoviesIntent {
    object FetchMovies : MoviesIntent()
}