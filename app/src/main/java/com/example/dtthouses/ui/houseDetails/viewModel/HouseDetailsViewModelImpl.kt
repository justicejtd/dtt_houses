package com.example.dtthouses.ui.houseDetails.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dtthouses.data.model.House
import com.example.dtthouses.useCases.house.HouseUseCases
import com.example.dtthouses.utils.ExceptionHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Handles any UI logic for HouseDetailsActivity.
 */
@HiltViewModel
class HouseDetailsViewModelImpl @Inject constructor(private val houseUseCases: HouseUseCases) :
    ViewModel(),
    HouseDetailsViewModel {
    private var _house = MutableLiveData<House>()
    override val house: LiveData<House> = _house

    override fun onLoadHouse(houseId: Int) {
        // Get and set house from database using house id.
        viewModelScope.launch(Dispatchers.IO + ExceptionHandler.getCoroutineExceptionHandler(null)) {
            _house.postValue(houseUseCases.getHouseById(houseId))
        }
    }
}