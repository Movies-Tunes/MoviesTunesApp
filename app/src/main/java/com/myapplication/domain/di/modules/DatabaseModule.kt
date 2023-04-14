package com.myapplication.domain.di.modules

import android.content.Context
import com.myapplication.data.localdatasource.MoviesDao
import com.myapplication.data.localdatasource.MoviesTunesDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {

    companion object {
        @Provides
        @Singleton
        fun providesMovieDao(
            @ApplicationContext context: Context,
        ): MoviesDao = MoviesTunesDatabase.getInstance(context).movieDao()
    }
}
