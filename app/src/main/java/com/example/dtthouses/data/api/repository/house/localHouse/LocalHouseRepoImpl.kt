package com.example.dtthouses.data.api.repository.house.localHouse

import com.example.dtthouses.data.database.dao.HouseDao
import com.example.dtthouses.data.model.House
import javax.inject.Inject

/**
 * House repository makes local calls from [HouseDao].
 */
class LocalHouseRepoImpl @Inject constructor(private val houseDao: HouseDao) : LocalHouseRepo {
    override suspend fun getHouses(): List<House> = houseDao.getAll()

    override suspend fun saveHouses(houses: List<House>) = houseDao.insertAll(houses)

    override suspend fun getHouseById(id: Int): House = houseDao.findById(id)

    override suspend fun getHousesBySearchQuery(searchQuery: String): List<House> =
        houseDao.findBySearchQuery(searchQuery)
}