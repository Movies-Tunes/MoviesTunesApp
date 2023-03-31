package com.myapplication.domain.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.myapplication.core.Constants
import com.myapplication.data.entities.TopRatedResult
import com.myapplication.data.entities.TopRatedResultItem
import com.myapplication.data.localdatasource.MoviesTunesDatabase
import com.myapplication.data.remotedatasource.TheMovieDbApiService
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class TopRatedResultMediator(
    private val query: String,
    private val database: MoviesTunesDatabase,
    private val networkService: TheMovieDbApiService,
) : RemoteMediator<Int, TopRatedResultItem>() {
    val moviesDao = database.movieDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, TopRatedResultItem>,
    ): MediatorResult {
        return try {
            // The network load method takes an optional after=<user.id>
            // parameter. For every page after the first, pass the last user
            // ID to let it continue from where it left off. For REFRESH,
            // pass null to load the first page.
            val loadKey = when (loadType) {
                LoadType.REFRESH -> {
                    state.anchorPosition?.minus(1) ?: 1
                }
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = true,
                        )
                    lastItem.id
                }
            }

            // Suspending network load via Retrofit. This doesn't need to be
            // wrapped in a withContext(Dispatcher.IO) { ... } block since
            // Retrofit's Coroutine CallAdapter dispatches on a worker
            // thread.
            val response = loadKey.let {
                networkService.getTopRatedMovies(
                    Constants.API_KEY,
                    it,
                    query,
                )
            }

            database.withTransaction {
                // Insert new users into database, which invalidates the
                // current PagingData, allowing Paging to present the updates
                // in the DB.
            moviesDao.insertAll(response.results)

            }

            MediatorResult.Success(
                endOfPaginationReached = response.page == null,
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}
