package com.example.dtthouses.data.api.service.house.repository

import com.example.dtthouses.data.model.House
import retrofit2.Response

/**
 * House repository call house data from house service
 */
interface HouseRepo {
    /**
     * Gets a list of houses from API.
     * @return Return a Response type List<House>
     */
    suspend fun getHouses(): Response<List<House>>
}