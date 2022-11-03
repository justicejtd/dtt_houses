package com.example.dtthouses.data.api.repository.house.httpHouse

import com.example.dtthouses.data.model.House

/**
 * House repository call house data from house service
 */
interface HttpHouseRepo {
    /**
     * Gets a list of houses from API.
     * @return Returns list of [House]
     */
    suspend fun getHouses(): List<House>
}