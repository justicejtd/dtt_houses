package com.example.dtthouses.data.api

import com.example.dtthouses.data.model.House
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * This repository works as a center point that handles all of the services.
 */
class ServiceRepository(private val service: ApiServiceImpl) {

    /**
     * Get an array list of houses.
     */
    suspend fun getHouses(): ArrayList<House> {
        // Get houses using coroutine
        return withContext(Dispatchers.IO) {
            service.getHouses()
        }
    }
}