package com.arash.altafi.chatandroid.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    @Named("BASE_URL")
    fun provideBaseURL(): String {
        return "https://jsonplaceholder.typicode.com"
    }

    @Singleton
    @Provides
    @Named("Arash_BASE_URL")
    fun providePagingBaseURL(): String {
        return "https://arashaltafi.ir/"
    }

    @Singleton
    @Provides
    @Named("jsonPlaceHolder")
    fun provideRetrofit(
        @Named("BASE_URL") baseURL: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()
    }

    @Singleton
    @Provides
    @Named("arashaltafi")
    fun providePagingRetrofit(
        @Named("Arash_BASE_URL") baseURL: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()
    }
}