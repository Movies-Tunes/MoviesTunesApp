package com.myapplication.domain.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.myapplication.core.Constants
import com.myapplication.data.entities.TopRatedResultItem
import com.myapplication.data.localdatasource.MoviesTunesDatabase
import com.myapplication.data.remotedatasource.data.api.TheMovieDbApiService
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class TopRatedResultMediator(
    private val query: String,
    private val database: MoviesTunesDatabase,
    private val networkService: TheMovieDbApiService,
) : RemoteMediator<Int, TopRatedResultItem>() {
    private val moviesDao = database.movieDao()
    private var key = 1

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, TopRatedResultItem>,
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> {
                    1
                }

                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    state.config.pageSize
                    val remoteKey = state.lastItemOrNull()
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = true,
                        )
                    remoteKey.id + 1
                }
            }

            val response = networkService.getTopRatedMovies(
                Constants.API_KEY,
                loadKey,
                query,
            )

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    moviesDao.deleteAllTopRatedMovie()
                }
                response.let {
                    moviesDao.insertAll(it.results)
                    Log.e("data", it.results.toString())
                }
            }

            MediatorResult.Success(
                endOfPaginationReached = response.results.isEmpty(),
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}
