package com.example.dtthouses.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

/**
 * Handles app permissions.
 */
object PermissionHelper {

    /**
     * Checks if user has granted permission for ACCESS_COARSE_LOCATION and ACCESS_FINE_LOCATION.
     * @param context Application/Activity context.
     */
    fun checkLocationPermission(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Request ACCESS_COARSE_LOCATION and ACCESS_FINE_LOCATION permissions.
     * @param activity The activity that is requesting the permission.
     * @param requestCode Request code
     */
    fun requestLocationPermission(activity: Activity, requestCode: Int) {
        ActivityCompat.requestPermissions(activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION), requestCode
        )
    }
}