package com.example.dtthouses.di.repository

import com.example.dtthouses.data.api.repository.house.httpHouse.HttpHouseRepo
import com.example.dtthouses.data.api.repository.house.httpHouse.HttpHouseRepoImpl
import com.example.dtthouses.data.api.repository.house.localHouse.LocalHouseRepo
import com.example.dtthouses.data.api.repository.house.localHouse.LocalHouseRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Repository module to inject repositories instances.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Binds [HttpHouseRepo] by using dagger-hilt.
     */
    @Binds
    @Singleton
    abstract fun bindsHttpHouseRepo(httpHouseRepoImpl: HttpHouseRepoImpl): HttpHouseRepo

    /**
     * Binds [LocalHouseRepo] by using dagger-hilt.
     */
    @Binds
    @Singleton
    abstract fun bindsLocalHouseRepo(localHouseRepoImpl: LocalHouseRepoImpl): LocalHouseRepo
}