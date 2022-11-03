package com.example.dtthouses.ui.houseDetails.viewModel

import androidx.lifecycle.LiveData
import com.example.dtthouses.data.model.House

/**
 * Exposed variables for HouseDetailsViewModel implementation.
 */
interface HouseDetailsViewModel {

    /**
     * LiveData House object to for showing the house details.
     */
    val house: LiveData<House>

    /**
     * Loads selected house based on provided house id.
     */
    fun onLoadHouse(houseId: Int)
}