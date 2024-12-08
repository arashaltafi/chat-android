package com.arash.altafi.chatandroid.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arash.altafi.chatandroid.data.model.FavoriteUserEntity

@Database(entities = [FavoriteUserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteUserDao(): FavoriteUserDao
}
