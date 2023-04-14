package com.myapplication

import android.app.Application
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
