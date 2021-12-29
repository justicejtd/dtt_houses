package com.example.dtthouses.ui.houseOverview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dtthouses.data.api.MainRepository
import com.example.dtthouses.data.model.House
import com.example.dtthouses.utils.Resource
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class HouseViewModel(private val repository: MainRepository) : ViewModel() {
    private val housesLiveData = MutableLiveData<Resource<ArrayList<House>>>()
    private lateinit var houses: ArrayList<House>
    private val isSearchNotFound = MutableLiveData<Boolean>(false)

    init {
        // Get houses from repository
        viewModelScope.launch {
            housesLiveData.postValue(Resource.loading(null))
            houses = repository.getHouses()
            if (houses.size == 0) {
                // If something goes wrong notify view with an error message
                housesLiveData.postValue(Resource.error("No houses found!", null))
            } else {
                // Sort houses by price (cheapest to expensive)
                houses.sortBy { house -> house.price }

                // Notify view when houses are fetched successfully
                housesLiveData.postValue(Resource.success(houses))
            }
        }
    }

    fun getHouses(): LiveData<Resource<ArrayList<House>>> {
        return housesLiveData
    }

    fun filterCourseListBySearch(input: String?) {
        val filteredHouses = houses.filter { house ->
            // Make a search pattern with combination of city and/or zip code
            var isFound = isFoundQueryFound(input.toString(), house.city, house.zip)
            if (!isFound) {
                // Make a search pattern with combination of zip code and/or city
                isFound = isFoundQueryFound(input.toString(), house.zip, house.city)
            }
            isFound
        }
        // If search is not found then show search not found image
        isSearchNotFound.value = filteredHouses.isEmpty()
        housesLiveData.postValue(Resource.success(ArrayList(filteredHouses)))
    }

    private fun isFoundQueryFound(input: String, value1: String, value2: String): Boolean {
        // Make a search pattern with combination of city and/or postal code
        var pattern = value1.uppercase().plus(value2.uppercase())

        // Remove any white spaces
        pattern = pattern.replace("\\s".toRegex(), "")

        // Set input value to uppercase and remove any whitespaces
        val query = input.uppercase().replace("\\s".toRegex(), "")

        // Check if house is found
        return pattern.contains(query)
    }

    fun getIsSearchNotFound(): LiveData<Boolean> {
        return isSearchNotFound
    }
}