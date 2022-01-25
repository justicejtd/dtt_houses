package com.example.dtthouses.data.api

/**
 * This repository works as a center point that handles all of the services.
 */
class ServiceRepository(private val service: ApiService) {

    /**
     * Get an array list of houses.
     */
    suspend fun getHouses() = service.getHouses()
}