package com.myapplication.domain.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.myapplication.core.Constants
import com.myapplication.data.entities.MovieDetail
import com.myapplication.data.entities.TopRatedResult
import com.myapplication.data.entities.TopRatedResultItem
import com.myapplication.data.localdatasource.MoviesDao
import com.myapplication.data.remotedatasource.TheMovieDbApiService

interface MovieRepository {
    suspend fun addMovie(movie: MovieDetail)
    suspend fun getAllMovies(page: Int): List<TopRatedResultItem>
    suspend fun getMovieDetails(movieId: Int): MovieDetail
    suspend fun updateMovie(movie: TopRatedResult): Int
    suspend fun deleteMovie(movie: TopRatedResult): Int
}

class MovieDataSource(
    private val service: TheMovieDbApiService,
    private val moviesDao: MoviesDao,
) : MovieRepository {

    private val _moviesDetails: MutableLiveData<MovieDetail> = MutableLiveData()
    val moviesDetails: LiveData<MovieDetail> get() = _moviesDetails

    override suspend fun addMovie(movie: MovieDetail) {
        moviesDao.insertMovieDetails(movie)
    }

    override suspend fun getAllMovies(page: Int): List<TopRatedResultItem> {
        return try {
            val topRatedMovies = service.getTopRatedMovies(Constants.API_KEY, page)
            runCatching {
                topRatedMovies.let {
                    it.results.forEach { safeMovie ->
                        moviesDao.insertMovie(safeMovie)
                    }
                }
            }
            topRatedMovies.results
        } catch (e: Exception) {
            var movies: List<TopRatedResultItem>? = null
            moviesDao.getAllMovies().collect {
                movies = it
            }
            e.printStackTrace()
            movies as List<TopRatedResultItem>
        }
    }

    override suspend fun getMovieDetails(movieId: Int): MovieDetail {
        return try {
            val movieDetail = service.getMovieDetails(movieId, Constants.API_KEY)
            _moviesDetails.postValue(movieDetail)
            Log.e("Detail", movieDetail.toString())
            kotlin.runCatching {
                moviesDao.insertMovieDetails(movieDetail)
            }
            movieDetail
        } catch (e: Exception) {
            var movies: MovieDetail? = null
            moviesDao.getMovieDetail(movieId).collect {
                movies = it
            }
            e.printStackTrace()
            movies!!
        }
    }

    override suspend fun updateMovie(movie: TopRatedResult): Int = 0

    override suspend fun deleteMovie(movie: TopRatedResult): Int = 0
}
