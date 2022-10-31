package com.example.dtthouses.ui.houseOverview.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.dtthouses.data.model.House
import com.example.dtthouses.utils.Resource

/**
 * Exposed variables for HouseViewModel implementation.
 */
interface HouseViewModel {
    /**
     * Returns LiveData of a list of filtered houses.
     * Used to keep track when list has been updated.
     */
    val filteredHouses: LiveData<Resource<List<House>>>

    /**
     * Keeps track if search query has found any houses.
     */
    val isSearchNotFound: LiveData<Boolean>

    /**
     * Return error message when something goes wrong during network call
     */
    val errorMessage: LiveData<String>

    /**
     * Filters list of houses based on city and zip code.
     */
    fun filterCourseListBySearch(input: String?)
}