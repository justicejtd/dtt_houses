package com.example.dtthouses.ui.houseOverview

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
import com.example.dtthouses.ui.houseDetails.HouseDetailsActivity
import com.google.gson.Gson


class HouseAdapter(var houses: List<House>, val context: Context) :
    RecyclerView.Adapter<HouseAdapter.ViewHolder>() {

    companion object DetailsIntents {
        const val DETAILS_INTENT_KEY = "com.example.dtthouses.ui.houseOverview.detailKey"
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        private val tvAddress: TextView = view.findViewById(R.id.tvAddress)
        private val tvNrOfBeds: TextView = view.findViewById(R.id.tvNrOfBedrooms)
        private val tvNrOfBaths: TextView = view.findViewById(R.id.tvNrOfBathrooms)
        private val tvNrOfLayers: TextView = view.findViewById(R.id.tvNrOfSize)
        private val tvLocationDistance: TextView = view.findViewById(R.id.tvLocationDistance)
        private val ivHouse: ImageView = view.findViewById(R.id.ivHouse)

        init {
            view.setOnClickListener {
                // Create intent
                val intent = Intent(context, HouseDetailsActivity::class.java)

                // Convert house object to json
                val json = Gson().toJson(houses[adapterPosition], House::class.java)

                // Set json string to intent string extra
                intent.putExtra(DETAILS_INTENT_KEY, json)

                // Show detail page
                startActivity(context, intent, null)
            }
        }

        fun setViews(house: House) {
            // Remove any space in the zip code
            val zip = house.zip.replace("\\s".toRegex(), "")

            tvPrice.text =
                context.getString(R.string.dolor_sign).plus(context.getString(R.string.price_format).format(house.price.toInt()))
            tvAddress.text = zip.plus(" ").plus(house.city)
            tvNrOfBeds.text = house.bedrooms.toString()
            tvNrOfBaths.text = house.bathrooms.toString()
            tvNrOfLayers.text = house.size.toString()
            tvLocationDistance.text =
                house.locationDistance.toString().plus(" ").plus(context.getString(R.string.km))
            ivHouse.setImageBitmap(house.getBitmap())
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

    fun addHouses(houses: ArrayList<House>) {
        this.houses = houses
        notifyDataSetChanged()
    }

    fun updateHousesLocation(latitude: Double, longitude: Double) {
        // Update all houses their location distance
        houses.forEach {
            // Set user location
            val startPoint = Location("UserLocation")
            startPoint.latitude = it.latitude
            startPoint.longitude = it.longitude

            // Set house location
            val endPoint = Location("HouseLocation")
            endPoint.latitude = latitude
            endPoint.longitude = longitude

            // Calculate distance in km
            val distance = startPoint.distanceTo(endPoint) / 1000

            // Set new distance
            it.locationDistance = distance.toInt()
        }
        notifyDataSetChanged()
    }
}