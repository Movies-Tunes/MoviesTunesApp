package com.myapplication.domain.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatchers

@Module
@InstallIn(SingletonComponent::class)
class DispatcherModule {
    @Provides
    @Singleton
    @DefaultDispatchers
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}
