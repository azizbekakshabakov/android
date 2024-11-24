package com.example.myfirstapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovieDao {
    @Insert
    suspend fun insert(movie: Movie)

    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<Movie>
}
