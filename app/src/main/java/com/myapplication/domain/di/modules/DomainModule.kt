package com.myapplication.domain.di.modules

import com.myapplication.domain.usecases.*
import com.myapplication.domain.usecases.favmovies.*
import com.myapplication.domain.usecases.moviedetails.GetMovieDetailsUseCase
import com.myapplication.domain.usecases.moviedetails.GetMovieDetailsUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DomainModule {
    @Binds
    abstract fun bindSignUseCase(
        signInUseCaseImpl: SignInUseCaseImpl,
    ): SignInUseCase

    @Binds
    abstract fun bindRegisterUseCase(
        registerUseCase: RegisterUseCaseImpl,
    ): RegisterUseCase

    @Binds
    abstract fun bindGetFavMoviesUseCase(
        getFavMoviesUseCase: GetFavMoviesUseCaseImpl,
    ): GetFavMoviesUseCase

    @Binds
    abstract fun bindIsFavMoviesUseCase(
        isFavMovieUseCase: IsFavMovieUseCaseImpl,
    ): IsFavMovieUseCase

    @Binds
    abstract fun bindSaveFavMovieUseCase(
        saveFavMoviesUseCase: SaveFavMoviesUseCaseImpl,
    ): SaveFavMoviesUseCase

    @Binds
    abstract fun bindDeleteFavMovieUseCase(
        deleteFavUseCase: DeleteFavUseCaseImpl,
    ): DeleteFavUseCase

    @Binds
    abstract fun bindDeleteFavMovieUseCase(
        getMovieDetailsUseCase: GetMovieDetailsUseCaseImpl,
    ): GetMovieDetailsUseCase
}
