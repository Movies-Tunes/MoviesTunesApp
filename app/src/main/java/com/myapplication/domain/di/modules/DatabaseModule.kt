package com.myapplication.domain.di.modules

import android.content.Context
import com.myapplication.data.localdatasource.MoviesDao
import com.myapplication.data.localdatasource.MoviesTunesDatabase
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
            moviesTunesDatabase: MoviesTunesDatabase,
        ): MoviesDao = moviesTunesDatabase.movieDao()

        @Provides
        @Singleton
        fun providesAppDatabase(
            @ApplicationContext context: Context,
        ): MoviesTunesDatabase = MoviesTunesDatabase.getInstance(context)
    }
}
