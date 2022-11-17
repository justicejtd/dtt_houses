package com.example.dtthouses.ui.houseDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dtthouses.R
import com.example.dtthouses.data.model.House
import com.example.dtthouses.ui.house.adapter.HouseAdapter.HouseAdapterConstants.DETAILS_INTENT_KEY
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.CameraUpdateFactory
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.example.dtthouses.data.api.service.ApiService
import com.example.dtthouses.databinding.ActivityHouseDetailsBinding
import com.example.dtthouses.databinding.ViewHouseDetailsBinding
import com.example.dtthouses.databinding.ViewHouseDetailsTopLayerBinding
import com.example.dtthouses.ui.houseDetails.viewModel.HouseDetailsViewModelImpl
import com.example.dtthouses.ui.house.adapter.HouseAdapter
import com.example.dtthouses.ui.houseDetails.viewModel.HouseDetailsViewModel
import com.example.dtthouses.utils.ImageHandler
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity for show all details of a house.
 */
@AndroidEntryPoint
class HouseDetailsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityHouseDetailsBinding
    private lateinit var houseDetailsTopLayer: ViewHouseDetailsTopLayerBinding
    private lateinit var houseDetails: ViewHouseDetailsBinding
    private lateinit var houseDetailsViewModel: HouseDetailsViewModel

    /**
     * Constants values of HouseDetails.
     */
    companion object {
        /**
         * Prefix for google navigation query.
         * Used for passing attributes to google maps.
         */
        private const val GOOGLE_NAVIGATION_QUERY_PREFIX = "google.navigation:q="

        /**
         * Google package name for map intent.
         */
        private const val GOOGLE_PACKAGE_NAME = "com.google.android.apps.maps"

        /**
         * Determines how far should the map be zoomed to the user location.
         */
        private const val MAPS_ZOOM_LEVEL = 15f

        /**
         * Determine how long should the zoom animation take.
         */
        private const val MAPS_ZOOM_DURATION = 1

        /**
         * Return location value equals to zero.
         * Can be used to check if location distance between current user and other house is zero.
         */
        private const val LOCATION_DISTANCE_ZERO = 0

        /**
         * Default house id, used if no house id is found from intent.
         */
        private const val DEFAULT_HOUSE_ID = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Setup view binding
        binding = ActivityHouseDetailsBinding.inflate(layoutInflater)
        houseDetailsTopLayer = binding.includeViewHouseDetailsTopLayer
        houseDetails = binding.includeViewHouseDetailsTopLayer.includeViewHouseDetails
        setContentView(binding.root)
        setupViewModel()
        setHouseDetails()
        setToolbarAndStatusBar()

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        val mapFragment: SupportMapFragment =
            houseDetailsTopLayer.mapFragmentContainer.getFragment()
        mapFragment.getMapAsync(this)

    }

    private fun setupViewModel() {
        houseDetailsViewModel = ViewModelProvider(this)[HouseDetailsViewModelImpl::class.java]

        // Load detail house from database based on house id
        houseDetailsViewModel.onLoadHouse(getHouseId())
    }

    override fun onMapReady(googleMap: GoogleMap) {
        houseDetailsViewModel.house.observe(this as LifecycleOwner) { house ->
            val latLng = LatLng(house.latitude, house.longitude)

            googleMap.uiSettings.setAllGesturesEnabled(false) // Disabled all gestures
            googleMap.addMarker(MarkerOptions().position(latLng))
            moveToCurrentLocation(latLng, googleMap) // Zoom map to the marker

            // Show google maps and show direction
            googleMap.setOnMapClickListener { showGoogleMap(latLng) }

            googleMap.setOnMarkerClickListener {
                showGoogleMap(latLng) // Show google maps from device
                true
            }

            googleMap.uiSettings.isZoomGesturesEnabled = false
            googleMap.uiSettings.isZoomControlsEnabled = false
            googleMap.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom = false
        }
    }

    private fun setToolbarAndStatusBar() {
        val toolbar = binding.toolbarHouseDetails

        // Set toolbar color to transparent
        toolbar.setBackgroundColor(Color.TRANSPARENT)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setHouseDetails() {
        houseDetailsViewModel.house.observe(this as LifecycleOwner) { house ->
            // Set house values to views
            houseDetailsTopLayer.tvPriceDetail.text =
                getString(R.string.dolor_sign).plus(getString(R.string.price_format).format(house.price.toInt()))
            houseDetails.tvNrOfBedrooms.text = house.bedrooms.toString()
            houseDetails.tvNrOfBathrooms.text = house.bathrooms.toString()
            houseDetails.tvNrOfSize.text = house.size.toString()
            houseDetailsTopLayer.tvDescriptionDetail.text = house.description

            // Handle location views
            setLocationViews(house)

            val imageUrl = ApiService.DTT_BASE_URL.plus(house.image)

            // Get and set house image
            ImageHandler.handleImage(imageUrl,
                this,
                binding.ivHouseDetail,
                HouseAdapter.DEFAULT_IMAGE)
        }
    }

    private fun setLocationViews(house: House) {
        if (house.locationDistance == LOCATION_DISTANCE_ZERO) {
            houseDetails.tvLocationDistance.visibility = View.GONE
            houseDetails.ivLocation.visibility = View.GONE
        } else {
            houseDetails.tvLocationDistance.text =
                house.locationDistance.toString().plus(" ").plus(getString(R.string.km))
        }

    }

    private fun showGoogleMap(latLng: LatLng) {
        val gmmIntentUri: Uri = Uri.parse(
            GOOGLE_NAVIGATION_QUERY_PREFIX.plus(latLng.latitude).plus(getString(R.string.comma))
                .plus(latLng.longitude)
        )
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage(GOOGLE_PACKAGE_NAME)
        startActivity(mapIntent)
    }

    private fun moveToCurrentLocation(currentLocation: LatLng, googleMap: GoogleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, MAPS_ZOOM_LEVEL))
        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn())
        // Zoom out to zoom level 15, animating with a duration of 2 seconds.
        googleMap.animateCamera(
            CameraUpdateFactory.zoomTo(MAPS_ZOOM_LEVEL),
            MAPS_ZOOM_DURATION,
            null
        )
    }

    private fun getHouseId(): Int = intent.getIntExtra(DETAILS_INTENT_KEY, DEFAULT_HOUSE_ID)
}