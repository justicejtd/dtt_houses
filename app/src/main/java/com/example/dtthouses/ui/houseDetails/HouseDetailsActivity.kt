package com.example.dtthouses.ui.houseDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
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
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.example.dtthouses.data.api.service.ApiService
import com.example.dtthouses.databinding.ActivityHouseDetailsBinding
import com.example.dtthouses.databinding.ViewHouseDetailsBinding
import com.example.dtthouses.databinding.ViewHouseDetailsTopLayerBinding
import com.example.dtthouses.ui.houseDetails.HouseDetailsActivity.HouseDetailsConstants.DEFAULT_HOUSE_ID
import com.example.dtthouses.ui.houseDetails.HouseDetailsActivity.HouseDetailsConstants.GOOGLE_NAVIGATION_QUERY_PREFIX
import com.example.dtthouses.ui.houseDetails.HouseDetailsActivity.HouseDetailsConstants.GOOGLE_PACKAGE_NAME
import com.example.dtthouses.ui.houseDetails.HouseDetailsActivity.HouseDetailsConstants.LOCATION_DISTANCE_ZERO
import com.example.dtthouses.ui.houseDetails.HouseDetailsActivity.HouseDetailsConstants.MAPS_ZOOM_LEVEL
import com.example.dtthouses.ui.houseDetails.HouseDetailsActivity.HouseDetailsConstants.MAPS_ZOOM_DURATION
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
    object HouseDetailsConstants {
        /**
         * Prefix for google navigation query.
         * Used for passing attributes to google maps.
         */
        const val GOOGLE_NAVIGATION_QUERY_PREFIX = "google.navigation:q="

        /**
         * Google package name for map intent.
         */
        const val GOOGLE_PACKAGE_NAME = "com.google.android.apps.maps"

        /**
         * Determines how far should the map be zoomed to the user location.
         */
        const val MAPS_ZOOM_LEVEL = 15f

        /**
         * Determine how long should the zoom animation take.
         */
        const val MAPS_ZOOM_DURATION = 2000

        /**
         * Return location value equals to zero.
         * Can be used to check if location distance between current user and other house is zero.
         */
        const val LOCATION_DISTANCE_ZERO = 0

        /**
         * Default house id, used if no house id is found from intent.
         */
        const val DEFAULT_HOUSE_ID = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Setup view binding
        binding = ActivityHouseDetailsBinding.inflate(layoutInflater)
        houseDetailsTopLayer = binding.includeViewHouseDetailsTopLayer
        houseDetails = binding.includeViewHouseDetailsTopLayer.includeViewHouseDetails
        setContentView(binding.root)

        // Setup viewModel
        setupViewModel()

        // Initialize house details
        setHouseDetails()

        // Set toolbar and status bar to transparent
        setToolbarAndStatusBar()

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragmentContainer) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

    }

    private fun setupViewModel() {
        houseDetailsViewModel = ViewModelProvider(this)[HouseDetailsViewModelImpl::class.java]

        // Load detail house from database based on house id
        houseDetailsViewModel.onLoadHouse(getHouseId())
    }

    override fun onMapReady(googleMap: GoogleMap) {
        houseDetailsViewModel.house.observe(this as LifecycleOwner) { house ->
            val latLng = LatLng(house.latitude, house.longitude)
            googleMap.addMarker(
                MarkerOptions()
                    .position(latLng)
            )

            // Zoom map to the marker
            moveToCurrentLocation(latLng, googleMap)

            // Show google maps and show direction
            googleMap.setOnMapClickListener {
                // Show google maps from device
                showGoogleMap(latLng)
            }

            googleMap.setOnMarkerClickListener {
                // Show google maps from device
                showGoogleMap(latLng)
                true
            }
        }
    }

    private fun setToolbarAndStatusBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbarHouseDetails)

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
            houseDetails.tvLocationDistance.text =
                house.locationDistance.toString().plus(" ").plus(getString(R.string.km))
            houseDetailsTopLayer.tvDescriptionDetail.text = house.description

            // Handle location views
            setLocationViews(house)

            val imageUrl = ApiService.DTT_BASE_URL.plus(house.image)

            // Get and set house image
            ImageHandler.handleImage(imageUrl, this,  binding.ivHouseDetail, HouseAdapter.DEFAULT_IMAGE)
        }
    }

    private fun setLocationViews(house: House) {
        val ivLocation = findViewById<ImageView>(R.id.ivLocation)

        if (house.locationDistance == LOCATION_DISTANCE_ZERO) {
            houseDetails.tvLocationDistance.visibility = View.GONE
            ivLocation.visibility = View.GONE
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