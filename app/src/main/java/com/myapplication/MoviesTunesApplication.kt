package com.myapplication

import android.app.Application
import com.myapplication.core.Constants
import com.myapplication.core.config.firebase.FirebaseConfig
import com.myapplication.data.localdatasource.MoviesTunesDatabase
import com.myapplication.data.remotedatasource.data.api.RetrofitImpl
import com.myapplication.data.remotedatasource.data.api.TheMovieDbApiService
import com.myapplication.domain.mediator.TopRatedResultMediator
import com.myapplication.domain.repository.MovieDataSource
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MoviesTunesApplication : Application() {
/*
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
    }*/
}
