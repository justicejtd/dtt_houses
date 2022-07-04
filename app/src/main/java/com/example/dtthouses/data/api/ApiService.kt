package com.example.dtthouses.data.api

import com.example.dtthouses.data.model.House
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

/**
 * API service that handles the endpoints.
 */
interface ApiService {

    /**
     * Returns an array list of houses from service.
     */
    @Headers("Accept: application/json", "Access-Key: $API_KEY")
    @GET(HOUSE_URL)
    suspend fun getHouses(): Response<ArrayList<House>>

    companion object {
        private lateinit var retrofitService: ApiService


        /**
         * Base URL of the service.
         * URL: https://intern.docker-dev.d-tt.nl
         */
        const val DTT_BASE_URL = "https://intern.development.d-tt.dev"

        /**
         * Endpoint to house(s).
         * Endpoint: /api/house
         */
        const val HOUSE_URL = "/api/house"

        /**
         * API key to get authorization to any endpoint.
         */
        const val API_KEY = "98bww4ezuzfePCYFxJEWyszbUXc7dxRx"

        /**
         * Returns a new instance of ApiService
         */
        fun getInstance(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(DTT_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofitService = retrofit.create(ApiService::class.java)

            return retrofitService
        }

    }
}