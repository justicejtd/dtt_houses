package com.example.dtthouses.di.useCases

import com.example.dtthouses.useCases.house.HouseUseCases
import com.example.dtthouses.useCases.house.HouseUseCasesImpl
import com.example.dtthouses.useCases.location.LocationUseCases
import com.example.dtthouses.useCases.location.LocationUseCasesImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * UseCases module inject useCases instances.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class UseCasesModule {

    /**
     * Binds [HouseUseCases] instance.
     */
    @Binds
    @Singleton
    abstract fun bindsHouseUseCases(houseUseCasesImpl: HouseUseCasesImpl): HouseUseCases

    /**
     * Binds [LocationUseCases] instance.
     */
    @Binds
    @Singleton
    abstract fun bindsLocationUseCases(locationUseCases: LocationUseCasesImpl): LocationUseCases
}