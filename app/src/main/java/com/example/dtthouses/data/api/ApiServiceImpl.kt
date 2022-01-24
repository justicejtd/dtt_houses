package com.example.dtthouses.data.api

import android.graphics.BitmapFactory
import android.util.Log
import com.bumptech.glide.Glide
import com.example.dtthouses.data.model.House
import com.google.gson.Gson
import java.io.IOException
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

class ApiServiceImpl : ApiService {
    private val baseUrl = "https://intern.docker-dev.d-tt.nl"

    override fun getHouses(): ArrayList<House> {
        val houses = ArrayList<House>()
        val endpoint = URL(baseUrl.plus("/api/house"))

        try {
            val httpsURLConnection = endpoint.openConnection()

            httpsURLConnection.setRequestProperty("Accept", "application/json")
            httpsURLConnection.setRequestProperty(
                "Access-Key",
                "98bww4ezuzfePCYFxJEWyszbUXc7dxRx"
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
                val imageInputStream = URL(baseUrl.plus(houses[i].image)).openStream()
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