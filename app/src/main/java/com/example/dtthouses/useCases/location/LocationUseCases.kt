package com.example.dtthouses.useCases.location

import android.location.Location

/**
 * Handles multiple location use cases.
 */
interface LocationUseCases {
    /**
     * Calculate location distance between provided start pont and point location
     */
    fun calculateLocationDistance(startPoint: Location, endPoint: Location): Int
}