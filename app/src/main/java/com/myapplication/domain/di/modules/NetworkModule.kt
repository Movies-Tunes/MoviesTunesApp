package com.myapplication.domain.di.modules

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.myapplication.core.config.firebase.FAVORITE_FILMS_COLLECTION
import com.myapplication.data.remotedatasource.data.api.RetrofitImpl
import com.myapplication.data.remotedatasource.data.api.TheMovieDbApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesFirestoreCollection(): CollectionReference =
        Firebase.firestore.collection(FAVORITE_FILMS_COLLECTION)

    @Provides
    @Singleton
    fun providesMoviesService(
        @ApplicationContext context: Context,
    ): TheMovieDbApiService = RetrofitImpl.getMovieService(context)

    @Provides
    fun providesAuthServiceFirebase(): FirebaseAuth = FirebaseAuth.getInstance()
}
