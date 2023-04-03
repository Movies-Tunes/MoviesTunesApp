package com.myapplication.domain.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.myapplication.MoviesTunesApplication
import com.myapplication.core.Constants
import com.myapplication.core.Constants.DEFAULT_QUERY
import com.myapplication.core.Constants.NETWORK_PAGE_SIZE
import com.myapplication.core.Response
import com.myapplication.core.error.CacheNotFoundException
import com.myapplication.data.entities.MovieDetail
import com.myapplication.data.entities.TopRatedResult
import com.myapplication.data.entities.TopRatedResultItem
import com.myapplication.data.localdatasource.MoviesDao
import com.myapplication.data.remotedatasource.data.api.TheMovieDbApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

interface MovieRepository {
    suspend fun addMovie(movie: MovieDetail)
    suspend fun getAllMovies(page: Int, query: String): Response<List<TopRatedResultItem>>
    suspend fun getMovieDetails(movieId: Long,query: String): Response<MovieDetail>
    suspend fun updateMovie(movie: TopRatedResult): Int
    suspend fun deleteMovie(movie: TopRatedResult): Int

    fun getAllMovies(query: String): Flow<PagingData<TopRatedResultItem>>
}

class MovieDataSource(
    private val service: TheMovieDbApiService,
    private val moviesDao: MoviesDao,
) : MovieRepository {

    private var topRatedMovies: List<TopRatedResultItem>? = null
    private var movieDetailDb: MovieDetail? = null

    override suspend fun addMovie(movie: MovieDetail) {
        moviesDao.insertMovieDetails(movie)
    }

    override suspend fun getAllMovies(
        page: Int,
        query: String,
    ): Response<List<TopRatedResultItem>> {
        return try {
            val movies = service.getTopRatedMovies(Constants.API_KEY, page, query)
            CoroutineScope(Dispatchers.IO).launch {
                movies.results.map { it }.onEach { topRatedResultItem ->
                    saveTopRatedMoviesInCache(topRatedResultItem)
                }
                moviesDao.getAllMovies().collect { moviesCache ->
                    moviesCache?.let {
                        topRatedMovies = it
                    }
                }
            }
            Response.Success(topRatedMovies ?: movies.results.sortedBy { it.id })
        } catch (e: Exception) {
            try {
                var movies: List<TopRatedResultItem>? = null
                moviesDao.getAllMovies().collect {
                    movies = it
                }
                Response.Success(movies ?: mutableListOf())
            } catch (e: Exception) {
                Response.Error(CacheNotFoundException())
            }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getAllMovies(query: String): Flow<PagingData<TopRatedResultItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
            ),
            initialKey = 1,
            remoteMediator = MoviesTunesApplication.instanceMediatorPaging,
        ) {
            MoviesPagingDataSource(query, service)
        }.flow
    }

    private suspend fun saveTopRatedMoviesInCache(topRatedMovies: TopRatedResultItem) {
        moviesDao.insertMovie(topRatedMovies)
    }

    override suspend fun getMovieDetails(movieId: Long, query: String): Response<MovieDetail> {
        return try {
            val movieDetail = service.getMovieDetails(movieId, Constants.API_KEY, query)
            CoroutineScope(Dispatchers.IO).launch {
                addMovie(movieDetail)
                moviesDao.getMovieDetail(movieId).collectLatest {
                    movieDetailDb = it
                }
            }
            Response.Success(movieDetail)
        } catch (e: Exception) {
            try {
                moviesDao.getMovieDetail(movieId).collectLatest {
                    movieDetailDb = it
                }
                Response.Success(movieDetailDb!!)
            } catch (e: Exception) {
                Response.Error(CacheNotFoundException())
            }
        }
    }

    override suspend fun updateMovie(movie: TopRatedResult): Int = 0

    override suspend fun deleteMovie(movie: TopRatedResult): Int = 0
}
