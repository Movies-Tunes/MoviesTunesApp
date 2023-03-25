package com.myapplication.data.localdatasource

import androidx.room.*
import com.myapplication.data.entities.TopRatedResultItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {
    @Insert
    suspend fun insertMovie(movie: TopRatedResultItem)

    @Query("SELECT * FROM movies_tb")
    fun getAllMovies(): Flow<List<TopRatedResultItem>>

    @Update
    suspend fun updateMovie(movie: TopRatedResultItem): Int

    @Delete
    suspend fun deleteMovie(movie: TopRatedResultItem): Int
}
