package com.example.dtthouses.data.api.repository.house.localHouse

import com.example.dtthouses.data.model.House
import com.example.dtthouses.data.database.AppDatabase

/**
 * House repository call house data from [AppDatabase].
 */
interface LocalHouseRepo {

    /**
     * Gets a list of houses from database.
     * @return Returns list of [House].
     */
    suspend fun getHouses(): List<House>

    /**
     * Save list of houses to database.
     * @param houses List of houses
     */
    suspend fun saveHouses(houses: List<House>)
}
