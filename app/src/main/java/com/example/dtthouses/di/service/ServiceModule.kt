package com.example.dtthouses.di.service

import com.example.dtthouses.data.api.service.ApiService
import com.example.dtthouses.data.api.service.house.HouseService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Service module to inject services instances.
 */
@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    /**
     * Provides [HouseService] instance.
     */
    @Provides
    @Singleton
    fun provideHouseService(): HouseService = ApiService.getHouseService()
}