package com.example.mvi_compose_sample.database

import androidx.room.Database
import com.example.mvi_compose_sample.feature.movies.data.Movie
import javax.inject.Singleton
import androidx.room.*

@Singleton
@Database(entities = [(Movie::class)], version = 1, exportSchema = false)
abstract class MovieDb : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}