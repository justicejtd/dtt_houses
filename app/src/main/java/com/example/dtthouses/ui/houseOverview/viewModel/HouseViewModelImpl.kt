package com.example.dtthouses.ui.houseOverview.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dtthouses.data.api.service.house.repository.HouseRepo
import com.example.dtthouses.data.model.House
import com.example.dtthouses.utils.Resource
import kotlinx.coroutines.*

/**
 * Handles any UI logic for HouseFragment.
 */
class HouseViewModelImpl(private val repository: HouseRepo) : ViewModel(), HouseViewModel {
class HouseViewModelImpl(private val houseUseCases: HouseUseCases) : ViewModel(), HouseViewModel {
    private var getHousesJob: Job? = null
    private var filterHousesJob: Job? = null
    private var houses: List<House> = listOf()
    private var _filteredHouses = MutableLiveData<Resource<List<House>>>()
    private val _isSearchNotFound = MutableLiveData(false)
    private val _errorMessage = MutableLiveData<String>()

    // Exposed variables
    override val filteredHouses: LiveData<Resource<List<House>>> = _filteredHouses
    override val isSearchNotFound: LiveData<Boolean> = _isSearchNotFound
    override val errorMessage: LiveData<String> = _errorMessage

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("$EXCEPTION_ERROR_PREFIX ${throwable.localizedMessage}")
    }

    /**
     * Constants values of HouseViewModel.
     */
    companion object {
        /**
         * Error message when no houses are found during network call.
         */
        private const val NO_HOUSES_FOUND_ERROR = "No houses found!"

        /**
         * Error message when there is not connection to internet or something went wrong
         * when trying to reach the server.
         */
        private const val ERROR_CONNECT_TO_SERVER_MSG = "Error: Could not communicate with server"

        /**
         * Exception prefix when there is an error during network calls
         */
        private const val EXCEPTION_ERROR_PREFIX = "Error: "
    }

    init {
        this.getHousesJob = CoroutineScope(Dispatchers.Main).launch {
            _filteredHouses.postValue(Resource.loading(null))
            try {
                // Uses extra threads for network call
                withContext(Dispatchers.IO + exceptionHandler) {
                    val response = repository.getHouses()
                    val housesResponse = houseUseCases.getHouses()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            val body = response.body()

                            if (body != null) {
                                // Set houses
                                houses = body

                                // Notify view when houses are fetched successfully
                                _filteredHouses.postValue(Resource.success(body))

                                // Sort houses by price (cheapest to expensive)
                                _filteredHouses.value?.data?.sortedBy { house -> house.price }
                            }

                            // Bug (for some reason "isSearchNotFound" needs to be false on init)
                            _isSearchNotFound.postValue(false)
                        } else {
                            // If something goes wrong notify view with an error message
                            _filteredHouses.postValue(Resource.error(NO_HOUSES_FOUND_ERROR, null))

                            onError("$EXCEPTION_ERROR_PREFIX ${response.message()} ")
                        }
                    }
                }
            } catch (ex: Exception) {
                // If something goes wrong notify view with an error message
                _filteredHouses.postValue(Resource.error(NO_HOUSES_FOUND_ERROR, null))

                onError("$EXCEPTION_ERROR_PREFIX $ERROR_CONNECT_TO_SERVER_MSG ")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        // Cancel all coroutines
        getHousesJob?.cancel()
        filterHousesJob?.cancel()
    }

    private fun onError(message: String) {
        _errorMessage.value = message
    }

    override fun filterCourseListBySearch(input: String?) {
        filterHousesJob = CoroutineScope(Dispatchers.Default).launch {
            val searchedHouses = houses.filter { house ->
                // Make a search pattern with combination of city and/or zip code
                var isFound = isFoundQueryFound(input.toString(), house.city, house.zip)

                if (!isFound) {
                    // Make a search pattern with combination of zip code and/or city
                    isFound = isFoundQueryFound(input.toString(), house.zip, house.city)
                }
                isFound
            }

            withContext(Dispatchers.Main) {
                // If search is not found then show search not found image
                _isSearchNotFound.postValue(searchedHouses.isEmpty())
                _filteredHouses.postValue(Resource.success(searchedHouses))
            }
        }
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
}