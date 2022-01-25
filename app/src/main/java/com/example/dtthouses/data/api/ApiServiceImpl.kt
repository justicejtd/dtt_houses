package com.example.dtthouses.data.api

import android.graphics.BitmapFactory
import com.example.dtthouses.data.api.ApiServiceImpl.ApiServiceConstants.API_KEY
import com.example.dtthouses.data.api.ApiServiceImpl.ApiServiceConstants.BASE_URL
import com.example.dtthouses.data.api.ApiServiceImpl.ApiServiceConstants.HOUSE_URL
import com.example.dtthouses.data.model.House
import com.google.gson.Gson
import java.io.IOException
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

/**
 * API service which handles all of the API calls.
 */
class ApiServiceImpl : ApiService {

    /**
     * API service constants values.
     */
    object ApiServiceConstants {
        /**
         * Base URL of the service.
         */
        const val BASE_URL = "https://intern.docker-dev.d-tt.nl"

        /**
         * Endpoint to house(s).
         */
        const val HOUSE_URL = "/api/house"

        /**
         * API key to get authorization to any endpoint.
         */
        const val API_KEY = "98bww4ezuzfePCYFxJEWyszbUXc7dxRx"
    }
    override fun getHouses(): ArrayList<House> {
        val houses = ArrayList<House>()
        val endpoint = URL(BASE_URL.plus(HOUSE_URL))

        try {
            val httpsURLConnection = endpoint.openConnection()

            httpsURLConnection.setRequestProperty("Accept", "application/json")
            httpsURLConnection.setRequestProperty(
                "Access-Key",
                API_KEY
            )
            httpsURLConnection.connect()

            val inputStream = httpsURLConnection.getInputStream()
            val scanner = Scanner(inputStream)
            val jsonString = scanner.useDelimiter("\\Z").next()

            // Convert json body to array list of houses
            Gson().fromJson(jsonString, Array<House>::class.java).toCollection(houses)

            // Set bitmaps for each house
            setBitmaps(houses)

        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return houses
    }

    private fun setBitmaps(houses: ArrayList<House>) {
        try {
            // Set bitmap for each house
            for (i in 0 until houses.size) {
                val imageInputStream = URL(BASE_URL.plus(houses[i].image)).openStream()
                val bitmap = BitmapFactory.decodeStream(imageInputStream)

                if (bitmap != null) {
                    houses[i].setBitmap(bitmap)
                }
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }
}