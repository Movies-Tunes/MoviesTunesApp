package com.myapplication.data.remotedatasource

import com.myapplication.data.entities.MovieDetail
import com.myapplication.data.entities.TopRatedResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDbApiService {
    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String, @Query("page") page: Int
    ): TopRatedResult

    @GET("movie/{movieId}")
    suspend fun getMovieDetails(
        @Query("api_key") apiKey: String, @Path("movieId") movieId: Int
    ): MovieDetail
}