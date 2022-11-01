package com.example.dtthouses.ui.houseDetails.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dtthouses.data.contstant.ErrorMessage.FACTORY_EXCEPTION_MSG
import com.example.dtthouses.ui.houseDetails.viewModel.HouseDetailsViewModel
import com.example.dtthouses.ui.houseDetails.viewModel.HouseDetailsViewModelImpl
import com.example.dtthouses.useCases.house.HouseUseCases

/**
 * Handles initialization of [HouseDetailsViewModel].
 */
class HouseDetailsVmFactory(private val houseUsesCases: HouseUseCases, private val houseId: Int) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HouseDetailsViewModelImpl::class.java)) {
            return HouseDetailsViewModelImpl(houseUsesCases, houseId) as T
        }
        throw IllegalArgumentException(FACTORY_EXCEPTION_MSG)
    }
}