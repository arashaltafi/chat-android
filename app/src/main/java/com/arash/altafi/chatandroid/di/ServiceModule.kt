package com.arash.altafi.chatandroid.di

import com.arash.altafi.chatandroid.data.api.ApiService
import com.arash.altafi.chatandroid.data.api.UploadService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Singleton
    @Provides
    fun provideApiService(@Named("jsonPlaceHolder") retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideUploadService(@Named("jsonPlaceHolder") retrofit: Retrofit): UploadService =
        retrofit.create(UploadService::class.java)

}
