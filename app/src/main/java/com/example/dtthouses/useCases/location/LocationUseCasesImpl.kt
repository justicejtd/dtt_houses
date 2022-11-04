package com.example.dtthouses.useCases.location

import android.location.Location
import javax.inject.Inject

/**
 * Implementation of [LocationUseCases] to handle certain location calculation.
 * @see [LocationUseCases].
 */
class LocationUseCasesImpl @Inject constructor(): LocationUseCases {

    /**
     * [LocationUseCases] constants values.
     */
    companion object {
        /**
         * Used for converting meters to kilometers.
         */
        const val DISTANCE_TO_KM = 1000
    }

     override fun calculateLocationDistance(startPoint: Location, endPoint: Location): Int {
        // Calculate distance in km
        return (startPoint.distanceTo(endPoint) / DISTANCE_TO_KM).toInt()
    }
}