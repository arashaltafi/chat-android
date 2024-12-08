package com.arash.altafi.chatandroid.di

import android.content.Context
import androidx.room.Room
import com.arash.altafi.chatandroid.data.db.AppDatabase
import com.arash.altafi.chatandroid.data.db.FavoriteUserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideFavoriteUserDao(database: AppDatabase): FavoriteUserDao {
        return database.favoriteUserDao()
    }
}