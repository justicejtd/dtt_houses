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
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dtthouses.R
import com.example.dtthouses.base.factories.ViewModelFactory
import com.example.dtthouses.data.api.ApiService
import com.example.dtthouses.data.api.ServiceRepository
import com.example.dtthouses.ui.houseOverview.HouseFragment.HouseFragmentConstants.FASTEST_INTERVAL_DURATION
import com.example.dtthouses.ui.houseOverview.HouseFragment.HouseFragmentConstants.INTERVAL_DURATION
import com.example.dtthouses.ui.houseOverview.HouseFragment.HouseFragmentConstants.MAX_WAIT_TIME
import com.example.dtthouses.utils.Status
import com.example.dtthouses.utils.makeClearableEditText
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import java.util.concurrent.TimeUnit


/**
 * Fragment showing an overview of houses.
 */
class HouseFragment : Fragment() {
    private lateinit var houseAdapter: HouseAdapter
    private val requestCode = 100
    private lateinit var houseViewModel: HouseViewModel
    private lateinit var rvHouses: RecyclerView
    private lateinit var viewSearchNotFound: View
    private lateinit var pbLoading: ProgressBar

    // FusedLocationProviderClient - Main class for receiving location updates.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // LocationRequest - Requirements for the location updates, i.e.,
    // how often you should receive updates, the priority, etc.
    private lateinit var locationRequest: LocationRequest

    // LocationCallback - Called when FusedLocationProviderClient
    // when there is a new Location
    private lateinit var locationCallback: LocationCallback

    // This will store current location info
    private var currentLocation: Location? = null

    /**
     * Constants values of House fragment.
     */
    object HouseFragmentConstants {
        /**
         * Location active update interval duration in seconds.
         */
        const val INTERVAL_DURATION: Long = 20

        /**
         * Location fastest active update interval duration in seconds.
         */
        const val FASTEST_INTERVAL_DURATION: Long = 60

        /**
         * Maximum time when batched location updates are delivered. (In seconds).
         */
        const val MAX_WAIT_TIME: Long = 2
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
        val viewModelFactory = ViewModelFactory(ServiceRepository(ApiService.getInstance()))
        houseViewModel = ViewModelProvider(this, viewModelFactory)[HouseViewModel::class.java]

        // Initialize fused location provider
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(activity as Activity)

        // Set location request
        setLocationRequest()

        // Setup observers
        setObservers()

        return view
    }

    private fun setObservers() {
        houseViewModel.getHouses().observe(this as LifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { houses ->
                        if (houses.isNotEmpty()) {
                            // Hide/gone progress bar
                            pbLoading.visibility = View.GONE

                            // Get houses
                            houseAdapter.addHouses(houses)

                            // If permission is granted
                            // check for gps permission and request location updates
                            checkForLocationPermission()

                            // Set location call back
                            setLocationCallBack()
                        }
                    }
                }
                Status.LOADING -> {
                    // Show progress bar
                    pbLoading.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    // Hide/gone progress bar
                    pbLoading.visibility = View.GONE

                    // Hide house overview list
                    rvHouses.visibility = View.GONE

                    // Show SearchNot found
                    viewSearchNotFound.visibility = View.VISIBLE
                }
            }
        }

        houseViewModel.getIsSearchNotFound().observe(this as LifecycleOwner) {
            if (it) {
                rvHouses.visibility = View.GONE
                viewSearchNotFound.visibility = View.VISIBLE
            } else {
                rvHouses.visibility = View.VISIBLE
                viewSearchNotFound.visibility = View.GONE
            }
        }

        // Show toast message when there is a network error
        houseViewModel.getErrorMessage().observe(this as LifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }
    }

    private fun setupUI(view: View) {
        houseAdapter = HouseAdapter(arrayListOf(), requireContext())
        pbLoading = view.findViewById(R.id.pbLoading)
        viewSearchNotFound = view.findViewById(R.id.viewSearchNotFound)
        rvHouses = view.findViewById(R.id.rvHouses)
        rvHouses.layoutManager = LinearLayoutManager(context)
        rvHouses.adapter = houseAdapter

        val etSearch = view.findViewById<EditText>(R.id.etSearchHome)

        // Set cancel button to search edit text view
        addRightCancelDrawable(etSearch)

        // Handle cancel search
        etSearch.makeClearableEditText(null, null)

        etSearch.doOnTextChanged { value, _, _, _ ->
            // Update list on search
            houseViewModel.filterCourseListBySearch(value.toString())
        }

        // Hide keyboard and clear search focus after search has been performed
        etSearch.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                hideSoftKeyboard()
                etSearch.isFocusable = false
                etSearch.isFocusableInTouchMode = true
                true
            } else {
                false
            }
        }
    }

    private fun addRightCancelDrawable(editText: EditText) {
        val cancel = ContextCompat.getDrawable(requireContext(), R.drawable.ic_close)
        cancel?.setBounds(0, 0, cancel.intrinsicWidth, cancel.intrinsicHeight)
        editText.setCompoundDrawables(null, null, cancel, null)
    }

    private fun hideSoftKeyboard() {
        // extension function to hide soft keyboard programmatically
        val imm = view?.let { getSystemService(it.context, InputMethodManager::class.java) }
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    /**
     * Start getting user current location.
     * If GPS sensor is turned off, then GPS location will be automatically opened.
     */
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
            // When location service is not enabled and permission is not denied
            // then open location settings
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

    /**
     * Handles location permission denied by hiding location icon and TextView distance.
     */
    fun handlePermissionDenied() {
        // Hide location icon and distance
        houseAdapter.hideLocationViews()
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
                    val latitude = it?.latitude
                    val longitude = it?.longitude

                    // Calculate and update house location distance
                    if (latitude != null && longitude != null) {
                        houseAdapter.updateHousesLocation(latitude, longitude)
                    }
                }
            }
        }
    }

    private fun setLocationRequest() {
        // Create location request
        locationRequest = LocationRequest.create().apply {
            // Sets the desired interval for active location updates.
            interval = TimeUnit.SECONDS.toMillis(INTERVAL_DURATION)

            // Sets the fastest rate for active location updates.
            // This interval is exact, and your application will never
            // receive updates more frequently than this value
            fastestInterval = TimeUnit.SECONDS.toMillis(FASTEST_INTERVAL_DURATION)

            // Sets the maximum time when batched location
            // updates are delivered. Updates may be
            // delivered sooner than this interval
            maxWaitTime = TimeUnit.MINUTES.toMillis(MAX_WAIT_TIME)

            priority = Priority.PRIORITY_HIGH_ACCURACY
        }
    }
}