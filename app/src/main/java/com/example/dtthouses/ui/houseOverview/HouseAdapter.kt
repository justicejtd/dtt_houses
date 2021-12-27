package com.example.dtthouses.ui.houseOverview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dtthouses.R
import com.example.dtthouses.data.model.House

class HouseAdapter(var houses: ArrayList<House>) :
    RecyclerView.Adapter<HouseAdapter.ViewHolder>() {

     inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        private val tvAddress: TextView = view.findViewById(R.id.tvAddress)
        private val tvNrOfBeds: TextView = view.findViewById(R.id.tvNrOfBedrooms)
        private val tvNrOfBaths: TextView = view.findViewById(R.id.tvNrOfBathrooms)
        private val tvNrOfLayers: TextView = view.findViewById(R.id.tvNrOfSize)
        private val tvLocationDistance: TextView = view.findViewById(R.id.tvLocationDistance)
        private val ivHouse: ImageView = view.findViewById(R.id.ivHouse)

        fun setViews(house: House) {
            tvPrice.text = house.price.toString()
            tvAddress.text = house.zip
            tvNrOfBeds.text = house.bedrooms.toString()
            tvNrOfBaths.text = house.bathrooms.toString()
            tvNrOfLayers.text = house.size.toString()
            tvLocationDistance.text = house.bedrooms.toString()
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
        this.houses.addAll(houses)
        notifyDataSetChanged()
    }
}