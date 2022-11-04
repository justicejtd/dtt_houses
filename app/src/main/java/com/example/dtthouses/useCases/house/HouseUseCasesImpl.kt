package com.example.dtthouses.useCases.house

import com.example.dtthouses.data.api.repository.house.httpHouse.HttpHouseRepo
import com.example.dtthouses.data.api.repository.house.localHouse.LocalHouseRepo
import com.example.dtthouses.data.model.House
import javax.inject.Inject

/**
 * Implementation of [HouseUseCases] to handle multiple house use cases either using the [HttpHouseRepo] or [LocalHouseRepo]
 * @see [HouseUseCases]
 */
class HouseUseCasesImpl @Inject constructor(
    private val httpHouseRepo: HttpHouseRepo,
    private val localHouseRepo: LocalHouseRepo,
) : HouseUseCases {
    override suspend fun getHouses(): List<House> {
        var houses = localHouseRepo.getHouses()

        if (houses.isEmpty().not()) {
            return houses
        }

        // Get houses from API if there nothing in the database.
        // It was assume that the houses data will not changed.
        houses = httpHouseRepo.getHouses()

        // Save house into database
        localHouseRepo.saveHouses(houses)

        return houses
    }

    override suspend fun getHouseById(id: Int): House = localHouseRepo.getHouseById(id)

    override suspend fun getHousesBySearchQuery(searchQuery: String): List<House> =
        localHouseRepo.getHousesBySearchQuery(searchQuery)

    override fun sortHouses(houses: List<House>): List<House> {
        return houses.sortedBy { house -> house.price }
    }

    override suspend fun updateHouses(houses: List<House>) {
        localHouseRepo.updateHouses(houses)
    }
}