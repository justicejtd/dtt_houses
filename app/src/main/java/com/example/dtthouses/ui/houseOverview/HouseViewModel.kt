package com.example.dtthouses.ui.houseOverview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dtthouses.data.api.ServiceRepository
import com.example.dtthouses.data.model.House
import com.example.dtthouses.ui.houseOverview.HouseViewModel.HouseViewModelConstants.NO_HOUSES_FOUND_ERROR
import com.example.dtthouses.utils.Resource
import kotlinx.coroutines.*
import kotlin.collections.ArrayList

/**
 * Handles any business logic for HouseFragment.
 */
class HouseViewModel(private val repository: ServiceRepository) : ViewModel() {
    private val errorMessage = MutableLiveData<String>()
    private val housesLiveData = MutableLiveData<Resource<ArrayList<House>>>()
    private lateinit var houses: ArrayList<House>
    private val isSearchNotFound = MutableLiveData(false)
    private var job: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    /**
     * Constants values of HouseViewModel.
     */
    object HouseViewModelConstants {
        /**
         * Error message when no houses are found during network call.
         */
        const val NO_HOUSES_FOUND_ERROR = "No houses found!"
    }

    init {
        housesLiveData.postValue(Resource.loading(null))
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getHouses()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val body = response.body()

                    if (body != null) {
                        houses = body
                        // Sort houses by price (cheapest to expensive)
                        houses.sortBy { house -> house.price }
                    }

                    // Notify view when houses are fetched successfully
                    housesLiveData.postValue(Resource.success(houses))
                } else {
                    // If something goes wrong notify view with an error message
                    housesLiveData.postValue(Resource.error(NO_HOUSES_FOUND_ERROR, null))

                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    private fun onError(message: String) {
        errorMessage.value = message
    }

    /**
     * Return LiveData of an array list of houses.
     * Used to keep track when list has been updated.
     */
    fun getHouses(): LiveData<Resource<ArrayList<House>>> {
        return housesLiveData
    }

    /**
     * Filters list of houses based on city and zip code.
     */
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

    /**
     * Keeps track if search query has found any houses.
     */
    fun getIsSearchNotFound(): LiveData<Boolean> {
        return isSearchNotFound
    }

    /**
     * Return error message when something goes wrong during network call
     */
    fun getErrorMessage(): LiveData<String> {
        return errorMessage
    }
}