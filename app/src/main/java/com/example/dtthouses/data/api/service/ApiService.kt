package com.example.dtthouses.data.api.service

import com.example.dtthouses.BuildConfig
import com.example.dtthouses.data.api.service.house.HouseService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Used for Http headers application/json accept type.
 */
const val ACCEPT_APPLICATION_JSON_TYPE = "Accept: application/json"

/**
 * API service that handles the endpoints.
 */
object ApiService {
    private lateinit var retrofitService: ApiService

    /**
     * Base URL of the service.
     * URL: https://intern.docker-dev.d-tt.nl
     */
    const val DTT_BASE_URL = "https://intern.development.d-tt.dev"

    /**
     * API key to get authorization to any endpoint.
     */
    private const val API_KEY = "98bww4ezuzfePCYFxJEWyszbUXc7dxRx"

    // Add parameters for okhttp client and logging
    private fun getRetrofit(): Retrofit {

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(chain.request().newBuilder().also {
                    it.addHeader("Access-Key", API_KEY)
                }.build())
            }.also { client ->
                if (BuildConfig.DEBUG) {
                    // Logger to log http request, response and body
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    client.addInterceptor(logging)
                }
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(DTT_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    fun getHouseService(): HouseService {
        return getRetrofit().create(HouseService::class.java)
    }
}