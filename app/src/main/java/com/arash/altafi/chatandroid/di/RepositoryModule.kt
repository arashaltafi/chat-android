package com.arash.altafi.chatandroid.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.arash.altafi.chatandroid.BuildConfig
import com.arash.altafi.chatandroid.data.api.UploadService
import com.arash.altafi.chatandroid.data.repository.SocketRepository
import com.arash.altafi.chatandroid.data.repository.UploadRepository
import com.arash.altafi.chatandroid.utils.EncryptionUtils
import com.arash.altafi.chatandroid.data.repository.DataStoreRepository
import com.arash.altafi.chatandroid.utils.JsonUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideDataStoreRepository(
        dataStore: DataStore<Preferences>,
        encryptionUtils: EncryptionUtils,
        jsonUtils: JsonUtils
    ) = DataStoreRepository(dataStore, encryptionUtils, jsonUtils)

    @Singleton
    @Provides
    fun provideDictionaryRepository(
        searchService: UploadService,
    ) = UploadRepository(searchService)

    @Provides
    @Singleton
    fun provideServerUrl(): String {
        return BuildConfig.BASE_URL
    }

    @Singleton
    @Provides
    fun provideSocketManager(
        serverUrl: String
    ) = SocketRepository(serverUrl)

}