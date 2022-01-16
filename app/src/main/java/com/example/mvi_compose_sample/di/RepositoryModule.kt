package com.example.mvi_compose_sample.di

import com.example.mvi_compose_sample.database.MovieDao
import com.example.mvi_compose_sample.feature.details.data.TrailerApi
import com.example.mvi_compose_sample.feature.movies.data.IMovieRepo
import com.example.mvi_compose_sample.feature.movies.data.MovieApi
import com.example.mvi_compose_sample.feature.movies.data.MovieRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideMovieRepository(
        movieDao: MovieDao,
        moviesApi: MovieApi,
        trailersApi: TrailerApi
    ): IMovieRepo {
        return MovieRepo(movieDao, moviesApi, trailersApi)
    }
}