package com.example.mvi_compose_sample.feature.movies.data

import com.example.mvi_compose_sample.utils.AppConstants.Companion.API_KEY_QUERY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton


@Singleton
interface MovieApi {

    companion object {
        const val POPULAR_MOVIES_QUERY: String = ("discover/movie?sort_by=popularity.desc")
    }

    @GET(POPULAR_MOVIES_QUERY)
    suspend fun getMostPopular(@Query(API_KEY_QUERY) apiKey: String): Response<MoviesResponse>
}