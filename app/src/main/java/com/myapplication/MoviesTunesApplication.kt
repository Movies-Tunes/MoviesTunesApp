package com.myapplication

import android.app.Application
import com.myapplication.data.localdatasource.MoviesTunesDatabase
import com.myapplication.data.remotedatasource.RetrofitImpl
import com.myapplication.data.remotedatasource.TheMovieDbApiService

class MoviesTunesApplication : Application() {
    val moviesTunesDatabase: MoviesTunesDatabase by lazy {
        MoviesTunesDatabase.getInstance(applicationContext)
    }
    val moviesService: TheMovieDbApiService by lazy {
        RetrofitImpl.getMovieService()
    }
}
