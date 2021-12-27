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

    init {
        // Get houses from repository
        viewModelScope.launch {
            housesLiveData.postValue(Resource.loading(null))
            val houses = repository.getHouses()
            if (houses.size == 0) {
                // If something goes wrong notify view with an error message
                housesLiveData.postValue(Resource.error("No houses found!", null))
            }
            else {
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
}