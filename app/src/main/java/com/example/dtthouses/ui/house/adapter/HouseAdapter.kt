package com.example.dtthouses.ui.house.adapter

import android.content.Context
import android.content.Intent
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.dtthouses.data.model.House
import com.example.dtthouses.R
import com.example.dtthouses.data.api.service.ApiService.DTT_BASE_URL
import com.example.dtthouses.ui.houseDetails.HouseDetailsActivity
import com.example.dtthouses.utils.ImageHandler


/**
 * Adapter for handling the list of houses.
 */
class HouseAdapter(var houses: List<House>, val context: Context) :
    RecyclerView.Adapter<HouseAdapter.ViewHolder>() {
    private var isLocationPermissionDenied = false

    /**
     * Constants values of HouseAdapter.
     */
    companion object HouseAdapterConstants {
        /**
         * Intent key for passing house object.
         */
        const val DETAILS_INTENT_KEY = "com.example.dtthouses.ui.houseOverview.detailKey"

        /**
         * Name of user location provider.
         */
        const val USER_LOCATION_PROVIDER = "UserLocation"

        /**
         * Name of house location provider.
         */
        const val HOUSE_LOCATION_PROVIDER = "HouseLocation"

        /**
         * Used for converting meters to kilometers.
         */
        const val DISTANCE_TO_KM = 1000

        /**
         * Default house image. Can be used when house image is not found during network call.
         */
        const val DEFAULT_IMAGE = R.drawable.house_placeholder
    }

    /**
     * Initialized and handles list item views for a house.
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        private val tvAddress: TextView = view.findViewById(R.id.tvAddress)
        private val tvNrOfBeds: TextView = view.findViewById(R.id.tvNrOfBedrooms)
        private val tvNrOfBaths: TextView = view.findViewById(R.id.tvNrOfBathrooms)
        private val tvNrOfLayers: TextView = view.findViewById(R.id.tvNrOfSize)
        private val tvLocationDistance: TextView = view.findViewById(R.id.tvLocationDistance)
        private val ivLocation: ImageView = view.findViewById(R.id.ivLocation)
        private val ivHouse: ImageView = view.findViewById(R.id.ivHouse)

        init {
            view.setOnClickListener {
                // Create intent
                val intent = Intent(context, HouseDetailsActivity::class.java)

                // Set house id to intent int extra
                val houseId = houses[adapterPosition].id
                intent.putExtra(DETAILS_INTENT_KEY, houseId)

                // Show detail page
                startActivity(context, intent, null)
            }
        }

        /**
         * Set house data to views.
         */
        fun setViews(house: House) {
            // Remove any space in the zip code
            val zip = house.zip.replace("\\s".toRegex(), "")

            tvPrice.text =
                context.getString(R.string.dolor_sign)
                    .plus(context.getString(R.string.price_format).format(house.price.toInt()))
            tvAddress.text = zip.plus(" ").plus(house.city)
            tvNrOfBeds.text = house.bedrooms.toString()
            tvNrOfBaths.text = house.bathrooms.toString()
            tvNrOfLayers.text = house.size.toString()

            val imageUrl = DTT_BASE_URL.plus(house.image)

            // Get and set house image
            ImageHandler.handleImage(imageUrl, context, ivHouse, DEFAULT_IMAGE)

            // Set location views visibility to GONE if location permission is denied
            setLocationViews(house)
        }

        private fun setLocationViews(house: House) {
            if (isLocationPermissionDenied) {
                tvLocationDistance.visibility = View.GONE
                ivLocation.visibility = View.GONE
            } else {
                tvLocationDistance.text =
                    house.locationDistance.toString().plus(" ").plus(context.getString(R.string.km))
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val listViewScheduleItem =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_house, parent, false)
        return ViewHolder(listViewScheduleItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setViews(houses[position])
    }

    override fun getItemCount() = houses.size

    /**
     * Add list of houses to recycler view.
     */
    fun addHouses(houses: List<House>) {
        this.houses = houses
        notifyDataSetChanged()
    }

    /**
     * Calculate and set distance between current and house location.
     */
    fun updateHousesLocation(latitude: Double, longitude: Double) {
        // Update all houses their location distance
        houses.forEach {
            // Set user location
            val startPoint = Location(USER_LOCATION_PROVIDER)
            startPoint.latitude = it.latitude
            startPoint.longitude = it.longitude

            // Set house location
            val endPoint = Location(HOUSE_LOCATION_PROVIDER)
            endPoint.latitude = latitude
            endPoint.longitude = longitude

            // Calculate distance in km
            val distance = startPoint.distanceTo(endPoint) / DISTANCE_TO_KM

            // Set new distance
            it.locationDistance = distance.toInt()
        }
        notifyDataSetChanged()
    }

    /**
     * Hide location icon and TextView.
     */
    fun hideLocationViews() {
        isLocationPermissionDenied = true
        notifyDataSetChanged()
    }
}