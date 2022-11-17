package com.example.dtthouses.utils

import android.location.LocationManager
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority

/**
 *
 */
object LocationProvider {
    /**
     * Location request code is used to check if location permission granted or not
     */
    const val LOCATION_REQUEST_CODE = 100

    /**
     * Location active update interval duration in seconds.
     */
    private const val INTERVAL_DURATION: Long = 1000

    /**
     * Location fastest active update interval duration in seconds.
     */
    private const val FASTEST_INTERVAL_DURATION: Long = 500

    /**
     * Maximum time when batched location updates are delivered. (In seconds).
     */
    private const val MAX_WAIT_TIME: Long = 1000

    /**
     * Sets if the location request should wait for accurate location.
     */
    private const val WAIT_FOR_ACCURATE_LOCATION: Boolean = false

    /**
     * Name of user location provider.
     */
    const val USER_LOCATION_PROVIDER = "UserLocation"

    /**
     * Name of house location provider.
     */
    const val HOUSE_LOCATION_PROVIDER = "HouseLocation"

    /**
     *  Creates location request.
     */
    fun getLocationRequest(): LocationRequest {
        return LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, INTERVAL_DURATION)
            .setWaitForAccurateLocation(WAIT_FOR_ACCURATE_LOCATION)
            .setMinUpdateIntervalMillis(FASTEST_INTERVAL_DURATION)
            .setMaxUpdateDelayMillis(MAX_WAIT_TIME).build()
    }

    /**
     * Check if GPS_PROVIDER AND NETWORK_PROVIDER is enabled
     */
    fun isLocationProviderEnabled(locationManager: LocationManager): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}