package com.myapplication

import android.app.Application
import com.myapplication.core.Constants
import com.myapplication.core.config.firebase.FirebaseConfig
import com.myapplication.data.localdatasource.MoviesTunesDatabase
import com.myapplication.data.remotedatasource.data.api.RetrofitImpl
import com.myapplication.data.remotedatasource.data.api.TheMovieDbApiService
import com.myapplication.domain.mediator.TopRatedResultMediator
import com.myapplication.domain.repository.MovieDataSource

class MoviesTunesApplication : Application() {
    private val moviesTunesDatabase: MoviesTunesDatabase by lazy {
        MoviesTunesDatabase.getInstance(applicationContext)
    }
    private val moviesService: TheMovieDbApiService by lazy {
        RetrofitImpl.getMovieService(applicationContext)
    }

    val movieDatasource by lazy {
        MovieDataSource(moviesService, moviesTunesDatabase.movieDao())
    }

    val firestoreCollection by lazy {
        FirebaseConfig().getCollectionReference()
    }

    val mediator: TopRatedResultMediator by lazy {
        if (instanceMediatorPaging == null) {
            instanceMediatorPaging = TopRatedResultMediator(
                Constants.DEFAULT_QUERY,
                moviesTunesDatabase,
                moviesService,
            )
        }
        instanceMediatorPaging as TopRatedResultMediator
    }

    companion object {
        var instanceMediatorPaging: TopRatedResultMediator? = null
    }
}
