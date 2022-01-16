package com.example.mvi_compose_sample.database

import androidx.room.*
import com.example.mvi_compose_sample.feature.movies.data.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie)

    @Query("SELECT id FROM movies")
    fun fetchFavouriteMovies(): List<Int?>

    @Delete()
    fun removeMovie(movie: Movie)

}