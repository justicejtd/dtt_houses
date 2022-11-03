package com.example.dtthouses.data.api.repository.house.httpHouse

import com.example.dtthouses.data.api.service.house.HouseService
import com.example.dtthouses.data.exception.GenericException
import com.example.dtthouses.data.exception.NetworkException
import com.example.dtthouses.data.model.House
import retrofit2.Response
import javax.inject.Inject

/**
 * House repository makes network calls from [HouseService].
 */
class HttpHouseRepoImpl @Inject constructor(private val service: HouseService) : HttpHouseRepo {

    @Throws(NetworkException::class, GenericException::class)
    override suspend fun getHouses(): List<House> {
        val response: Response<List<House>>

        try {
             response = service.getHouses()
        } catch (ex: Exception) {
            throw GenericException()
        }

        // Throw network exception if something goes during network call.
        if (response.isSuccessful.not()) throw NetworkException()

        // Returns houses or throws generic exception if houses is equals to null.
        return response.body() ?: throw GenericException()
    }
}
