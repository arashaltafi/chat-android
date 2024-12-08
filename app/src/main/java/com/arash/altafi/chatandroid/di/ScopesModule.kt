package com.arash.altafi.chatandroid.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ScopesModule {

    @DefaultScope
    @Singleton
    @Provides
    fun providesDefaultCoroutineScope(
        @DefaultDispatcher dispatcher: CoroutineDispatcher
    ): CoroutineScope {
        return CoroutineScope(dispatcher + SupervisorJob())
    }

    @IoScope
    @Singleton
    @Provides
    fun providesIoCoroutineScope(
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): CoroutineScope {
        return CoroutineScope(dispatcher + SupervisorJob())
    }
}