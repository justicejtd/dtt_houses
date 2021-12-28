package com.example.dtthouses.ui.houseOverview

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dtthouses.R
import com.example.dtthouses.base.ViewModelFactory
import com.example.dtthouses.data.api.ApiServiceImpl
import com.example.dtthouses.data.api.MainRepository
import com.example.dtthouses.utils.Status
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import java.util.concurrent.TimeUnit

class HouseFragment : Fragment() {
    private val houseAdapter = HouseAdapter(arrayListOf())
    private val requestCode = 100
    private lateinit var houseViewModel: HouseViewModel

    // FusedLocationProviderClient - Main class for receiving location updates.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // LocationRequest - Requirements for the location updates, i.e.,
    // how often you should receive updates, the priority, etc.
    private lateinit var locationRequest: LocationRequest

    // LocationCallback - Called when FusedLocationProviderClient
    // has a new Location
    private lateinit var locationCallback: LocationCallback

    // This will store current location info
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_house, container, false)

        // Initialize views
        setupUI(view)

        // Setup view model
        val viewModelFactory = ViewModelFactory(MainRepository(ApiServiceImpl()))
        houseViewModel = ViewModelProvider(this, viewModelFactory)[HouseViewModel::class.java]

        // Initialize fused location provider
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(activity as Activity)

        // Set location request
        setLocationRequest()

        // Set location call back
        setLocationCallBack()

        // Setup observer
        houseViewModel.getHouses().observe(this as LifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { houses ->
                        // Get houses
                        houseAdapter.addHouses(houses)

                        // Check for gps permission and request location updates if permission is granted
                        checkForLocationPermission()
                    }
                }
                Status.LOADING -> {
                    // TODO show splash screen
                }
                else -> {}
            }
        })

        return view
    }

    private fun setupUI(view: View) {
        val rvHouses = view.findViewById<RecyclerView>(R.id.rvHouses)
        rvHouses.layoutManager = LinearLayoutManager(context)
        rvHouses.adapter = houseAdapter
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {
        // Initialize location manager
        val locationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Check if gps or network provider is on, if true then get last location
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        ) {
            // When location service is enabled, get last location
            fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                val location = it.result
                // Check if location is null
                if (location != null) {
                    // Calculate and update house location distance
                    houseAdapter.updateHousesLocation(location.latitude, location.longitude)
                } else {
                    // Request location updates
                    fusedLocationProviderClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.myLooper()!!
                    )
                }
            }
        } else {
            // when location service is not enabled and permission is denied then open location settings
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    ACCESS_COARSE_LOCATION
                ) != PERMISSION_DENIED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    ACCESS_FINE_LOCATION
                ) != PERMISSION_DENIED
            ) {
                startActivity(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }
    }

    private fun checkForLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_COARSE_LOCATION
            ) == PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_FINE_LOCATION
            ) == PERMISSION_GRANTED
        ) {
            // If permission is granted started getting user location
            getCurrentLocation()
        } else {
            // If permission is not granted ask for location permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    ACCESS_FINE_LOCATION,
                    ACCESS_COARSE_LOCATION
                ), requestCode
            )
        }
    }

    private fun setLocationCallBack() {
        // Create location call back
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.let { super.onLocationResult(it) }
                locationResult.lastLocation.let {
                    currentLocation = it
                    val latitude = it.latitude
                    val longitude = it.longitude

                    // Calculate and update house location distance
                    houseAdapter.updateHousesLocation(latitude, longitude)
                }
            }
        }
    }

    private fun setLocationRequest() {
        // Create location request
        locationRequest = LocationRequest.create().apply {
            // Sets the desired interval for active location updates.
            interval = TimeUnit.SECONDS.toMillis(20)

            // Sets the fastest rate for active location updates.
            // This interval is exact, and your application will never
            // receive updates more frequently than this value
            fastestInterval = TimeUnit.SECONDS.toMillis(60)

            // Sets the maximum time when batched location
            // updates are delivered. Updates may be
            // delivered sooner than this interval
            maxWaitTime = TimeUnit.MINUTES.toMillis(2)

            priority = PRIORITY_HIGH_ACCURACY
        }
    }
}