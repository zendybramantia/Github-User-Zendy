package com.zendy.githubuser.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavUser::class], version = 1, exportSchema = false)
abstract class FavUserDatabase : RoomDatabase() {
    abstract fun favUserDao(): FavUserDao

    companion object {
        @Volatile
        private var INSTANCE: FavUserDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): FavUserDatabase {
            if (INSTANCE == null) {
                synchronized(FavUserDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        FavUserDatabase::class.java, "fav_user_database")
                        .build()
                }
            }
            return INSTANCE as FavUserDatabase
        }
    }
}