package com.example.dtthouses.useCases.house

import com.example.dtthouses.data.api.repository.house.httpHouse.HttpHouseRepo
import com.example.dtthouses.data.api.repository.house.localHouse.LocalHouseRepo
import com.example.dtthouses.data.model.House

/**
 * Handles multiple house use cases either using the [HttpHouseRepo] or [LocalHouseRepo]
 */
interface HouseUseCases {

    /**
     * Gets a list of [House] either from the AppDatabase or from the API.
     */
    suspend fun getHouses(): List<House>

    /**
     * Get house based on inputted house id.
     */
    suspend fun getHouseById(id: Int): House
}