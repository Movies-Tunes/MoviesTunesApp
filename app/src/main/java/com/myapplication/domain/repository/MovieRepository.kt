package com.myapplication.domain.repository

import android.util.Log
import com.myapplication.core.Constants
import com.myapplication.core.Response
import com.myapplication.core.error.CacheNotFoundException
import com.myapplication.data.entities.MovieDetail
import com.myapplication.data.entities.TopRatedResult
import com.myapplication.data.entities.TopRatedResultItem
import com.myapplication.data.localdatasource.MoviesDao
import com.myapplication.data.remotedatasource.TheMovieDbApiService

interface MovieRepository {
    suspend fun addMovie(movie: MovieDetail)
    suspend fun getAllMovies(page: Int): Response<List<TopRatedResultItem>>
    suspend fun getMovieDetails(movieId: Int): Response<MovieDetail>
    suspend fun updateMovie(movie: TopRatedResult): Int
    suspend fun deleteMovie(movie: TopRatedResult): Int
}

class MovieDataSource(
    private val service: TheMovieDbApiService,
    private val moviesDao: MoviesDao,
) : MovieRepository {


    override suspend fun addMovie(movie: MovieDetail) {
        moviesDao.insertMovieDetails(movie)
    }

    override suspend fun getAllMovies(page: Int): Response<List<TopRatedResultItem>> {
        return try {
            val topRatedMovies = service.getTopRatedMovies(Constants.API_KEY, page)
            topRatedMovies.let {
                it.results.forEach { safeMovie ->
                    moviesDao.insertMovie(safeMovie)
                }
            }
            Response.Success(topRatedMovies.results)

        } catch (e: Exception) {
            try {
                var movies: List<TopRatedResultItem>? = null
                moviesDao.getAllMovies().collect {
                    movies = it
                }
                Response.Success(movies!!)

            } catch (e: Exception) {
                Response.Error(CacheNotFoundException())
            }

        }
    }

    override suspend fun getMovieDetails(movieId: Int): Response<MovieDetail> {
        return try {
            val movieDetail = service.getMovieDetails(movieId, Constants.API_KEY)
            Log.e("Detail", movieDetail.toString())
            kotlin.runCatching {
                moviesDao.insertMovieDetails(movieDetail)
            }
            Response.Success(movieDetail)
        } catch (e: Exception) {
            try {
                var movies: MovieDetail? = null
                moviesDao.getMovieDetail(movieId).collect {
                    movies = it
                }
                Response.Success(movies!!)

            } catch (e: Exception) {
                Response.Error(CacheNotFoundException())
            }

        }
    }

    override suspend fun updateMovie(movie: TopRatedResult): Int = 0

    override suspend fun deleteMovie(movie: TopRatedResult): Int = 0
}
