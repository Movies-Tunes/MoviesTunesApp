package com.myapplication.domain.di.modules

import com.myapplication.domain.usecases.RegisterUseCase
import com.myapplication.domain.usecases.RegisterUseCaseImpl
import com.myapplication.domain.usecases.SignInUseCase
import com.myapplication.domain.usecases.SignInUseCaseImpl
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
}
