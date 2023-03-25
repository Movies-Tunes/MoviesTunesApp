package com.myapplication.data.remotedatasource

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.myapplication.core.Constants.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private val moshi = Moshi
    .Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
class RetrofitImpl {
    companion object {

        @Volatile
        private var INSTANCE: Retrofit? = null

        private fun getRetrofit(context: Context): Retrofit {
            if (INSTANCE == null) {
                synchronized(this) {
                    val client = OkHttpClient.Builder()
                        .addInterceptor(ChuckerInterceptor.Builder(context).build()).build()
                    INSTANCE = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(MoshiConverterFactory.create(moshi))
                        .client(client)
                        .build()
                }
            }
            return INSTANCE!!
        }

        fun getMovieService(context: Context): TheMovieDbApiService {
            return getRetrofit(context).create(TheMovieDbApiService::class.java)
        }
    }
}
