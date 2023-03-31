package com.myapplication.domain.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.myapplication.core.Constants.API_KEY
import com.myapplication.data.entities.TopRatedResultItem
import com.myapplication.data.remotedatasource.TheMovieDbApiService
import okio.IOException
import retrofit2.HttpException

class MoviesPagingDataSource(
    private val tmdbService: TheMovieDbApiService
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
                page = pageIndex
            )

            LoadResult.Page(
                data = response.results,
                prevKey = if (pageIndex == 1) null else pageIndex - 1,
                nextKey = pageIndex + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }

    }

}