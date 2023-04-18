package com.myapplication.domain.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.myapplication.core.Constants.API_KEY
import com.myapplication.core.Constants.NETWORK_PAGE_SIZE
import com.myapplication.data.entities.TopRatedResultItem
import com.myapplication.data.remotedatasource.data.api.TheMovieDbApiService
import okio.IOException
import retrofit2.HttpException

class MoviesPagingDataSource(
    private val query: String,
    private val tmdbService: TheMovieDbApiService,
) : PagingSource<Int, TopRatedResultItem>() {

    override fun getRefreshKey(state: PagingState<Int, TopRatedResultItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TopRatedResultItem> {
        return try {
            val pageIndex = params.key ?: 1
            val response = tmdbService.getTopRatedMovies(
                apiKey = API_KEY,
                page = pageIndex,
                query,
            )
            val nextKey = if (response.results.isEmpty()) {
                null
            } else {
                pageIndex + (params.loadSize / NETWORK_PAGE_SIZE)
            }

            LoadResult.Page(
                data = response.results,
                prevKey = null,
                nextKey = response.page + 1,
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}
