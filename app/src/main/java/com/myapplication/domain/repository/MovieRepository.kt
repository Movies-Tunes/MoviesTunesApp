package com.myapplication.domain.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.myapplication.core.Constants
import com.myapplication.core.Constants.NETWORK_PAGE_SIZE
import com.myapplication.core.Response
import com.myapplication.core.error.CacheNotFoundException
import com.myapplication.data.entities.MovieDetail
import com.myapplication.data.entities.TopRatedResult
import com.myapplication.data.entities.TopRatedResultItem
import com.myapplication.data.localdatasource.MoviesTunesDatabase
import com.myapplication.data.remotedatasource.data.api.TheMovieDbApiService
import com.myapplication.domain.mediator.TopRatedResultMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

interface MovieRepository {
    suspend fun addMovie(movie: MovieDetail)
    suspend fun getMovieDetails(movieId: Long, query: String): Response<MovieDetail>
    suspend fun updateMovie(movie: TopRatedResult): Long
    suspend fun deleteMovie(movie: TopRatedResult): Long

    fun getAllMovies(query: String): Flow<PagingData<TopRatedResultItem>>
}

class MovieDataSource @Inject constructor(
    private val service: TheMovieDbApiService,
    private val moviesTunesDatabase: MoviesTunesDatabase,
) : MovieRepository {

    private var movieDetailDb: MovieDetail? = null

    override suspend fun addMovie(movie: MovieDetail) {
        moviesTunesDatabase.movieDao().insertMovieDetails(movie)
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getAllMovies(query: String): Flow<PagingData<TopRatedResultItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
            ),
            remoteMediator = TopRatedResultMediator(
                query,
                moviesTunesDatabase,
                service,
            ),
            pagingSourceFactory = {
                /*moviesTunesDatabase.movieDao().getAllMovies()*/
                MoviesPagingDataSource(Locale.getDefault().toLanguageTag(), service)
            },
        ).flow
    }

    private suspend fun saveTopRatedMoviesInCache(topRatedMovies: TopRatedResultItem) {
        moviesTunesDatabase.movieDao().insertMovie(topRatedMovies)
    }

    override suspend fun getMovieDetails(movieId: Long, query: String): Response<MovieDetail> {
        return try {
            val movieDetail = service.getMovieDetails(movieId, Constants.API_KEY, query)
            CoroutineScope(Dispatchers.IO).launch {
                addMovie(movieDetail)
                moviesTunesDatabase.movieDao().getMovieDetail(movieId).collectLatest {
                    movieDetailDb = it
                }
            }
            Response.Success(movieDetail)
        } catch (e: Exception) {
            try {
                moviesTunesDatabase.movieDao().getMovieDetail(movieId).collectLatest {
                    movieDetailDb = it
                }
                Response.Success(movieDetailDb!!)
            } catch (e: Exception) {
                Response.Error(CacheNotFoundException())
            }
        }
    }

    override suspend fun updateMovie(movie: TopRatedResult): Long = 0

    override suspend fun deleteMovie(movie: TopRatedResult): Long = 0
}
