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
     * Gets house based on the provided house id.
     */
    suspend fun getHouseById(id: Int): House

    /**
     * Gets house based on the provided search query.
     */
    suspend fun getHousesBySearchQuery(searchQuery: String): List<House>

    /**
     * Update all houses data in the local database.
     */
    suspend fun updateHouses(houses: List<House>)

    /**
     * Returns a list of sorted houses.
     */
    suspend fun sortHouses(houses: List<House>): List<House>
}