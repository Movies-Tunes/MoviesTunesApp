package com.myapplication.domain.di.modules

import com.myapplication.domain.repository.MovieDataSource
import com.myapplication.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface MovieViewModelModule {
    @Binds
    @ViewModelScoped
    fun bindsRepository(
        movieDataSource: MovieDataSource,
    ): MovieRepository
}
