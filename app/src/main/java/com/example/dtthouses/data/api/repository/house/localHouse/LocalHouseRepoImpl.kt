package com.example.dtthouses.data.api.repository.house.localHouse

import com.example.dtthouses.data.database.dao.HouseDao
import com.example.dtthouses.data.model.House

/**
 * House repository makes local calls from [HouseDao].
 */
class LocalHouseRepoImpl(private val houseDao: HouseDao) : LocalHouseRepo {
    override suspend fun getHouses(): List<House> = houseDao.getAll()

    override suspend fun saveHouses(houses: List<House>) {
        houseDao.insertAll(houses)
    }
}