package com.example.dtthouses.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dtthouses.data.database.dao.HouseDao
import com.example.dtthouses.data.model.House

/**
 * Application database using Room.
 */
@Database(version = 1, entities = [House::class])
abstract class AppDatabase : RoomDatabase() {
    companion object {
        // Database name
        private const val DB_NAME = "dtt-real-estate-db"

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        /**
         * Creates an instance of AppDatabase
         */
        fun getInstance(context: Context): AppDatabase {
            // Synchronized block to prevent multiple calls at a time from different threads.
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Creates Room database instance
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME).build()
        }
    }

    /**
     * Returns an instance of HouseDao
     */
    abstract fun getHouseDao(): HouseDao
}