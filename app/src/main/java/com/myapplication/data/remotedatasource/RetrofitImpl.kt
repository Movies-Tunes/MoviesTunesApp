package com.myapplication.data.remotedatasource

import com.myapplication.core.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RetrofitImpl {
    companion object {

        @Volatile
        private var INSTANCE: Retrofit? = null

        private fun getRetrofit(): Retrofit {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(MoshiConverterFactory.create()).build()
                }

            }
            return INSTANCE!!
        }

        fun getMovieService() : TheMovieDbApiService {
            return getRetrofit().create(TheMovieDbApiService::class.java)
        }
        }
}