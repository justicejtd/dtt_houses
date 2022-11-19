package com.example.dtthouses.di.database

import android.app.Application
import com.example.dtthouses.data.database.AppDatabase
import com.example.dtthouses.data.database.dao.HouseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Database module to inject database and dao's instances.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides [AppDatabase] instance.
     */
    @Provides
    @Singleton
    fun providesDatabase(application: Application): AppDatabase =
        AppDatabase.getInstance(application)


    /**
     * Provides [HouseDao] instance.
     */
    @Provides
    @Singleton
    fun providesHouseDao(appDatabase: AppDatabase): HouseDao = appDatabase.getHouseDao()

}