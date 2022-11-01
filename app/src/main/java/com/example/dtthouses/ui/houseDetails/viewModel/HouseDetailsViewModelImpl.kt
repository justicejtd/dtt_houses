package com.example.dtthouses.ui.houseDetails.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dtthouses.data.model.House
import com.example.dtthouses.useCases.house.HouseUseCases
import com.example.dtthouses.utils.ExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Handles any UI logic for HouseDetailsActivity.
 */
class HouseDetailsViewModelImpl(private val houseUseCases: HouseUseCases, private val houseId: Int) : ViewModel(),
    HouseDetailsViewModel {
    private var _house = MutableLiveData<House>()
    override val house: LiveData<House> = _house

    init {
        // Get and set house from database using house id.
        loadHouse()
    }

    private fun loadHouse() {
        viewModelScope.launch(Dispatchers.IO + ExceptionHandler.getCoroutineExceptionHandler(null)) {
            _house.postValue(houseUseCases.getHouseById(houseId))
        }
    }
}