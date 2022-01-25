package com.example.dtthouses.ui.houseDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.dtthouses.R
import com.example.dtthouses.data.model.House
import com.example.dtthouses.ui.houseOverview.HouseAdapter.DetailsIntents.DETAILS_INTENT_KEY
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
import com.example.dtthouses.ui.houseDetails.HouseDetailsActivity.HouseDetailsConstants.GOOGLE_NAVIGATION_QUERY_PREFIX
import com.example.dtthouses.ui.houseDetails.HouseDetailsActivity.HouseDetailsConstants.GOOGLE_PACKAGE_NAME
import com.example.dtthouses.ui.houseDetails.HouseDetailsActivity.HouseDetailsConstants.MAPS_ZOOM_LEVEL
import com.example.dtthouses.ui.houseDetails.HouseDetailsActivity.HouseDetailsConstants.MAPS_ZOOM_DURATION


class HouseDetailsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var house: House

    object HouseDetailsConstants {
        const val GOOGLE_NAVIGATION_QUERY_PREFIX = "google.navigation:q="
        const val GOOGLE_PACKAGE_NAME = "com.google.android.apps.maps"
        const val MAPS_ZOOM_LEVEL = 15f
        const val MAPS_ZOOM_DURATION = 2000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_house_details)

        // Initialize views
        val tvPriceDetail = findViewById<TextView>(R.id.tvPriceDetail)
        val tvNrOfBedroomsDetail = findViewById<TextView>(R.id.tvNrOfBedrooms)
        val tvNrOfBathroomsDetail = findViewById<TextView>(R.id.tvNrOfBathrooms)
        val tvNrOfSizeDetail = findViewById<TextView>(R.id.tvNrOfSize)
        val tvLocationDistanceDetail = findViewById<TextView>(R.id.tvLocationDistance)
        val tvDescriptionDetail = findViewById<TextView>(R.id.tvDescriptionDetail)
        val ivHouseDetail = findViewById<ImageView>(R.id.ivHouseDetail)

        // Set toolbar and status bar to transparent
        setToolbarAndStatusBar()

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
        ivHouseDetail.setImageBitmap(house.getBitmap())

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
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
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(MAPS_ZOOM_LEVEL), MAPS_ZOOM_DURATION, null)
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