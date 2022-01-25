package com.example.dtthouses.ui.houseDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.dtthouses.R
import com.example.dtthouses.data.model.House
import com.example.dtthouses.ui.houseOverview.HouseAdapter.HouseAdapterConstants.DETAILS_INTENT_KEY
import com.google.android.gms.maps.*
import com.google.gson.Gson

import com.google.android.gms.maps.model.MarkerOptions

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.CameraUpdateFactory
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.WindowManager

import androidx.appcompat.widget.Toolbar
import com.example.dtthouses.data.api.ApiService
import com.example.dtthouses.ui.houseDetails.HouseDetailsActivity.HouseDetailsConstants.GOOGLE_NAVIGATION_QUERY_PREFIX
import com.example.dtthouses.ui.houseDetails.HouseDetailsActivity.HouseDetailsConstants.GOOGLE_PACKAGE_NAME
import com.example.dtthouses.ui.houseDetails.HouseDetailsActivity.HouseDetailsConstants.MAPS_ZOOM_LEVEL
import com.example.dtthouses.ui.houseDetails.HouseDetailsActivity.HouseDetailsConstants.MAPS_ZOOM_DURATION
import com.example.dtthouses.ui.houseOverview.HouseAdapter
import com.example.dtthouses.utils.ImageHandler

/**
 * Activity for show all details of a house.
 */
class HouseDetailsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var house: House
    private lateinit var tvPriceDetail: TextView
    private lateinit var tvNrOfBedroomsDetail: TextView
    private lateinit var tvNrOfBathroomsDetail: TextView
    private lateinit var tvNrOfSizeDetail: TextView
    private lateinit var tvLocationDistanceDetail: TextView
    private lateinit var tvDescriptionDetail: TextView
    private lateinit var ivHouseDetail: ImageView

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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_house_details)

        // Initialize views
        setupViews()

        // Initialize house details
        setHouseDetails()

        // Set toolbar and status bar to transparent
        setToolbarAndStatusBar()


        // Get the SupportMapFragment and request notification when the map is ready to be used.
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragmentContainer) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
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

    private fun setupViews() {
        tvPriceDetail = findViewById(R.id.tvPriceDetail)
        tvNrOfBedroomsDetail = findViewById(R.id.tvNrOfBedrooms)
        tvNrOfBathroomsDetail = findViewById(R.id.tvNrOfBathrooms)
        tvNrOfSizeDetail = findViewById(R.id.tvNrOfSize)
        tvLocationDistanceDetail = findViewById(R.id.tvLocationDistance)
        tvDescriptionDetail = findViewById(R.id.tvDescriptionDetail)
        ivHouseDetail = findViewById(R.id.ivHouseDetail)
    }

    private fun setHouseDetails() {
        // Get intent
        val json = intent.getStringExtra(DETAILS_INTENT_KEY)

        // Get house from intent
        house = Gson().fromJson(json, House::class.java)

        // Set house values to views
        tvPriceDetail.text =
            getString(R.string.dolor_sign).plus(getString(R.string.price_format).format(house.price.toInt()))
        tvNrOfBedroomsDetail.text = house.bedrooms.toString()
        tvNrOfBathroomsDetail.text = house.bathrooms.toString()
        tvNrOfSizeDetail.text = house.size.toString()
        tvLocationDistanceDetail.text =
            house.locationDistance.toString().plus(" ").plus(getString(R.string.km))
        tvDescriptionDetail.text = house.description

        val imageUrl = ApiService.DTT_BASE_URL.plus(house.image)

        // Get and set house image
        ImageHandler.handleImage(imageUrl, this, ivHouseDetail, HouseAdapter.DEFAULT_IMAGE)
    }

    private fun showGoogleMap(latLng: LatLng) {
        val gmmIntentUri: Uri = Uri.parse(
            GOOGLE_NAVIGATION_QUERY_PREFIX.plus(latLng.latitude).plus(",").plus(latLng.longitude)
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

    private fun setToolbarAndStatusBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbarHouseDetails)

        // Set toolbar color to transparent
        toolbar.setBackgroundColor(Color.TRANSPARENT)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Set status bar color to transparent
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.TRANSPARENT
    }
}