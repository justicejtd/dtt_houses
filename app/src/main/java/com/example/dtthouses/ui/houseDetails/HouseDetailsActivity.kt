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

import android.view.Window
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat


class HouseDetailsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var house: House

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
        val location = LatLng(house.latitude, house.longitude)
        googleMap.addMarker(
            MarkerOptions()
                .position(location)
                .title("Marker in house location")
        )

        // Zoom map to the marker
        moveToCurrentLocation(location, googleMap)

        // Show google maps and show direction
        googleMap.setOnMapClickListener {
            val gmmIntentUri: Uri = Uri.parse(
                "google.navigation:q=".plus(location.latitude).plus(",").plus(location.longitude)
            )
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }

    private fun moveToCurrentLocation(currentLocation: LatLng, googleMap: GoogleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn())
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15f), 2000, null)
    }

    private fun setToolbarAndStatusBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbarHouseDetails)

        // Toolbar :: Transparent
        toolbar.setBackgroundColor(Color.TRANSPARENT)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Status bar :: Transparent
        val window: Window = this.window

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.TRANSPARENT
    }
}