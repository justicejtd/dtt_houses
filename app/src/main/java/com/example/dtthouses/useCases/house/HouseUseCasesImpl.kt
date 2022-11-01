package com.example.dtthouses.useCases.house

import com.example.dtthouses.data.api.repository.house.httpHouse.HttpHouseRepo
import com.example.dtthouses.data.api.repository.house.localHouse.LocalHouseRepo
import com.example.dtthouses.data.model.House

/**
 * Implementation of [HouseUseCases] to handle multiple house use cases either using the [HttpHouseRepo] or [LocalHouseRepo]
 * @see [HouseUseCases]
 */
class HouseUseCasesImpl(
    private val httpHouseRepo: HttpHouseRepo,
    private val localHouseRepo: LocalHouseRepo
) : HouseUseCases {
    override suspend fun getHouses(): List<House> {
        var houses = localHouseRepo.getHouses()

        // Get houses from API if there nothing in the database.
        // It was assume that the houses data will not changed.
        if (houses.isEmpty()) {
            houses = httpHouseRepo.getHouses()

            // Save house into database
            localHouseRepo.saveHouses(houses)
        }
        return houses
    }

    override suspend fun getHouseById(id: Int): House = localHouseRepo.getHouseById(id)
}