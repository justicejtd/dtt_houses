package com.example.dtthouses.data.api

import com.example.dtthouses.data.model.House
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ServiceRepository(private val service: ApiServiceImpl) {

    suspend fun getHouses(): ArrayList<House> {
        // Get houses using coroutine
        return withContext(Dispatchers.IO) {
            service.getHouses()
        }
    }
}