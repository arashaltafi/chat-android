package com.arash.altafi.chatandroid.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.arash.altafi.chatandroid.utils.EncryptionUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent

val Context.dataStore by preferencesDataStore(name = "secure_preferences")

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context) = context.dataStore

    @Provides
    @Singleton
    fun provideEncryptionUtils(): EncryptionUtils {
        return EncryptionUtils()
    }
}