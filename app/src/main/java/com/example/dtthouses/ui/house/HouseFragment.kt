package com.example.dtthouses.ui.house

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dtthouses.R
import com.example.dtthouses.databinding.FragmentHouseBinding
import com.example.dtthouses.ui.house.adapter.HouseAdapter
import com.example.dtthouses.ui.house.viewModel.HouseViewModel
import com.example.dtthouses.ui.house.viewModel.HouseViewModelImpl
import com.example.dtthouses.utils.*
import com.example.dtthouses.utils.LocationProvider.HOUSE_LOCATION_PROVIDER
import com.example.dtthouses.utils.LocationProvider.LOCATION_REQUEST_CODE
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

/**
 * Fragment showing an overview of houses.
 */
@AndroidEntryPoint
class HouseFragment : Fragment() {
    private lateinit var houseAdapter: HouseAdapter
    private lateinit var houseViewModel: HouseViewModel
    private lateinit var binding: FragmentHouseBinding

    // Main class for receiving location updates.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHouseBinding.inflate(inflater, container, false)
        setupUI()
        setupViewModel()
        setupFusedLocationProvider()
        setObservers()
        checkForLocationPermission()
        return binding.root
    }

    private fun setupViewModel() {
        houseViewModel = ViewModelProvider(this)[HouseViewModelImpl::class.java]
    }

    private fun setupFusedLocationProvider() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(activity as Activity)
    }

    private fun setObservers() {
        houseViewModel.filteredHouses.observe(this as LifecycleOwner) {
            val pbLoading = binding.pbLoading

            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { houses ->
                        if (houses.isNotEmpty()) {
                            pbLoading.visibility = View.GONE
                            houseAdapter.addHouses(houses)
                        }
                    }
                }
                Status.LOADING -> pbLoading.visibility = View.VISIBLE
                Status.ERROR -> {
                    pbLoading.visibility = View.GONE
                    binding.rvHouses.visibility = View.GONE
                    binding.viewSearchNotFound.root.visibility = View.VISIBLE
                }
            }
        }

        houseViewModel.isSearchNotFound.observe(this as LifecycleOwner) {
            if (it) {
                binding.rvHouses.visibility = View.GONE
                binding.viewSearchNotFound.root.visibility = View.VISIBLE
            } else {
                binding.rvHouses.visibility = View.VISIBLE
                binding.viewSearchNotFound.root.visibility = View.GONE
            }
        }

        // Show toast message when there is a network error
        houseViewModel.errorMessage.observe(this as LifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }
    }

    private fun setupUI() {
        houseAdapter = HouseAdapter(arrayListOf(), requireContext())
        houseAdapter.setHasStableIds(true)
        binding.rvHouses.layoutManager = LinearLayoutManager(context)
        binding.rvHouses.adapter = houseAdapter
        val etSearch = binding.etSearchHome

        // Set cancel button to search edit text view
        addRightCancelDrawable(etSearch)

        // Handle cancel search
        etSearch.makeClearableEditText(null, null)

        etSearch.doOnTextChanged { value, _, _, _ ->
            // Update list on search
            houseViewModel.onSearchTextChanged(value.toString())
        }

        // Hide keyboard and clear search focus after search has been performed
        etSearch.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                KeyboardHelper.hideSoftKeyboard(view)
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

    /**
     * Starts getting user current location.
     * If GPS sensor is turned off, then GPS location will be automatically opened.
     */
    fun getCurrentLocation() {
        val locationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Check if gps or network provider is on, if true then set last location
        if (LocationProvider.isLocationProviderEnabled(locationManager)) setLastLocation()
        else {
            // Open location settings when location permission is granted and GPS off.
            if (PermissionHelper.checkLocationPermission(requireContext())) {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun setLastLocation() {
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {
            if (it.result != null) {
                onUserLocationUpdate(it.result)
            } else {
                // Request location updates
                fusedLocationProviderClient.requestLocationUpdates(LocationProvider.getLocationRequest(),
                    getLocationCallBack(),
                    Looper.myLooper()!!)
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
        if (PermissionHelper.checkLocationPermission(requireContext())) {
            // If permission is granted start getting user location
            getCurrentLocation()
        } else {
            // If permission is not granted ask for location permission
            PermissionHelper.requestLocationPermission(requireActivity(), LOCATION_REQUEST_CODE)
        }
    }

    private fun getLocationCallBack(): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.let { super.onLocationResult(it) }
                onUserLocationUpdate(locationResult.lastLocation)
            }
        }
    }

    private fun onUserLocationUpdate(userLocation: Location?) {
        val houseLocation = Location(HOUSE_LOCATION_PROVIDER)
        userLocation?.let {
            houseViewModel.onUserLocationUpdate(userLocation, houseLocation)
        }
    }
}