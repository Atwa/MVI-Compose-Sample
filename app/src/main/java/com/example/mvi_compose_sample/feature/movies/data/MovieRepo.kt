package com.example.mvi_compose_sample.feature.movies.data

import com.example.mvi_compose_sample.BuildConfig
import com.example.mvi_compose_sample.database.MovieDao
import com.example.mvi_compose_sample.feature.details.data.TrailerApi
import com.example.mvi_compose_sample.feature.details.data.TrailerResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepo @Inject constructor(
    private val movieDao: MovieDao,
    private val movieApi: MovieApi,
    private val trailerApi: TrailerApi
) : IMovieRepo {

    override suspend fun getPopularMovies(): Response<MoviesResponse> {
        return movieApi.getMostPopular(BuildConfig.API_KEY)
    }

    override suspend fun fetchMovieTrailers(movieId: Int): Response<TrailerResponse> {
        return trailerApi.getMovieTrailer(movieId, BuildConfig.API_KEY)
    }

    override fun isMovieLiked(id: Int): Boolean {
        return movieDao.fetchFavouriteMovies().contains(id)
    }

    override fun changeLikeState(movie: Movie, newLikeState: Boolean) {
        if (newLikeState) movieDao.insertMovie(movie)
        else movieDao.removeMovie(movie)
    }

}