package com.myapplication.core

import com.myapplication.BuildConfig

object Constants {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val BASE_POSTER_PATH = "https://image.tmdb.org/t/p/w342"
    const val API_KEY = BuildConfig.API_KEY
    const val NETWORK_PAGE_SIZE = 3
    const val DEFAULT_QUERY = "pt-BR"
}
