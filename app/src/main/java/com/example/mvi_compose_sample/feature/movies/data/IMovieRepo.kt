package com.example.mvi_compose_sample.feature.movies.data

import com.example.mvi_compose_sample.feature.details.data.TrailerResponse
import retrofit2.Response

interface IMovieRepo {
    suspend fun getPopularMovies(): Response<MoviesResponse>
    suspend fun fetchMovieTrailers(movieId: Int): Response<TrailerResponse>
    fun isMovieLiked(id: Int): Boolean
    fun changeLikeState(movie: Movie, newLikeState: Boolean)
}