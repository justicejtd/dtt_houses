package com.example.dtthouses.data.api

import com.example.dtthouses.data.model.House

/**
 * API service that handles the endpoints.
 */
interface ApiService {
    /**
     * Returns an array list of houses from service.
     */
    fun getHouses(): ArrayList<House>
}