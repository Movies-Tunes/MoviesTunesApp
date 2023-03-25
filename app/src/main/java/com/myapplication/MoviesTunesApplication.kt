package com.myapplication

import android.app.Application
import com.myapplication.data.localdatasource.MoviesTunesDatabase
import com.myapplication.data.remotedatasource.RetrofitImpl
import com.myapplication.data.remotedatasource.TheMovieDbApiService
import com.myapplication.domain.repository.MovieDataSource

class MoviesTunesApplication : Application() {
    val moviesTunesDatabase: MoviesTunesDatabase by lazy {
        MoviesTunesDatabase.getInstance(applicationContext)
    }
    val moviesService: TheMovieDbApiService by lazy {
        RetrofitImpl.getMovieService(applicationContext)
    }

    val movieDatasource by lazy {
        MovieDataSource(moviesService, moviesTunesDatabase.movieDao())
    }
}
