package com.example.dtthouses.data.api.service.house.repository

import com.example.dtthouses.data.api.service.house.HouseService

/**
 * House repository makes network calls from house service
 */
class HouseRepoImpl(private val service: HouseService) : HouseRepo {
    override suspend fun getHouses() = service.getHouses()
}