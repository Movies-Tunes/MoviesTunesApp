package com.myapplication.data.localdatasource

import androidx.paging.PagingSource
import androidx.room.*
import com.myapplication.data.entities.MovieDetail
import com.myapplication.data.entities.TopRatedResultItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: TopRatedResultItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(result: List<TopRatedResultItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetails(movie: MovieDetail)

    @Query("SELECT * FROM top_rated_tb")
    fun getAllMovies(): PagingSource<Int, TopRatedResultItem>

    @Query("SELECT * FROM movie_detail_tb WHERE id = :id")
    fun getMovieDetail(id: Long): Flow<MovieDetail?>

    @Update
    suspend fun updateMovie(movie: TopRatedResultItem): Int

    @Delete
    suspend fun deleteMovie(movie: TopRatedResultItem): Int

    @Query("DELETE FROM top_rated_tb")
    suspend fun deleteAllTopRatedMovie(): Int
}
