package com.example.dtthouses.data.api.service.house

import com.example.dtthouses.data.api.service.ACCEPT_APPLICATION_JSON_TYPE
import com.example.dtthouses.data.model.House
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface HouseService {
    companion object {
        /**
         * Endpoint to get house(s).
         * Endpoint: /api/house
         */
        private const val HOUSE_URL = "/api/house"
    }

    /**
     * Gets a list of houses from API.
     * @return Return a Response type List<House>
     */
    @Headers(ACCEPT_APPLICATION_JSON_TYPE)
    @GET(HOUSE_URL)
    suspend fun getHouses(): Response<List<House>>
}