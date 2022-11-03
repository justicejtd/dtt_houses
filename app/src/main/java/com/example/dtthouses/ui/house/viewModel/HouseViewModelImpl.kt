package com.example.dtthouses.ui.house.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dtthouses.data.api.repository.house.exception.NoHouseException
import com.example.dtthouses.data.exception.GenericException
import com.example.dtthouses.data.exception.NetworkException
import com.example.dtthouses.data.model.House
import com.example.dtthouses.useCases.house.HouseUseCases
import com.example.dtthouses.utils.ExceptionHandler
import com.example.dtthouses.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Handles any UI logic for HouseFragment.
 */
@HiltViewModel
class HouseViewModelImpl @Inject constructor(private val houseUseCases: HouseUseCases) :
    ViewModel(), HouseViewModel {
    private var houses: List<House> = listOf()
    private var _filteredHouses = MutableLiveData<Resource<List<House>>>()
    private val _isSearchNotFound = MutableLiveData(false)
    private val _errorMessage = MutableLiveData<String>()

    // Exposed variables
    override val filteredHouses: LiveData<Resource<List<House>>> = _filteredHouses
    override val isSearchNotFound: LiveData<Boolean> = _isSearchNotFound
    override val errorMessage: LiveData<String> = _errorMessage

    init { loadHouses() }

    private fun loadHouses() {
        viewModelScope.launch(Dispatchers.IO + ExceptionHandler.getCoroutineExceptionHandler { _, throwable ->
            throwable.message?.let { onError(it) }
        }) {
            try {
                _filteredHouses.postValue(Resource.loading(null))
                houses = houseUseCases.getHouses()

                // Notify view when houses are fetched successfully
                _filteredHouses.postValue(Resource.success(houses))

            } catch (ex: NoHouseException) {
                // If there are no houses found from the API show an error message.
                _filteredHouses.postValue(Resource.error(ex.message.toString(), null))
                onError(ex.message.toString())
            } catch (ex: GenericException) {
                // Todo: Show error image or message instead of the loading progress bar
                onError(ex.message.toString())
            } catch (ex: NetworkException) {
                onError(ex.message.toString())
            }
        }
    }

    private fun onError(message: String) {
        _errorMessage.value = message
    }

    override fun onSearchTextChanged(input: String?) {
        viewModelScope.launch(Dispatchers.Default) {
            val searchedHouses = houseUseCases.getHousesBySearchQuery(input.toString())

            // If search is not found then show search not found image
            _isSearchNotFound.postValue(searchedHouses.isEmpty())
            _filteredHouses.postValue(Resource.success(searchedHouses))
        }
    }
}