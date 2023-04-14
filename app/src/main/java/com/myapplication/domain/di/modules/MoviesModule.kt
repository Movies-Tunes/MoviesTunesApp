package com.myapplication.domain.di.modules

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.myapplication.core.Constants
import com.myapplication.core.config.firebase.FAVORITE_FILMS_COLLECTION
import com.myapplication.data.remotedatasource.data.api.TheMovieDbApiService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MoviesModule {
    @Provides
    @Singleton
    fun providesFirestoreCollection(): CollectionReference =
        Firebase.firestore.collection(FAVORITE_FILMS_COLLECTION)

    @Provides
    @Singleton
    fun providesMoviesService(
        @ApplicationContext context: Context,
        @ChuckerInterceptorOkHttpClient client: OkHttpClient,
        @MoshiAdapterFactory moshi: Moshi,
    ): TheMovieDbApiService =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(TheMovieDbApiService::class.java)

    @Provides
    fun providesAuthServiceFirebase(): FirebaseAuth = FirebaseAuth.getInstance()
}
