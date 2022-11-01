package com.example.dtthouses.ui.house.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dtthouses.data.contstant.ErrorMessage.FACTORY_EXCEPTION_MSG
import com.example.dtthouses.ui.house.viewModel.HouseViewModelImpl
import com.example.dtthouses.useCases.house.HouseUseCases

/**
 * Handles initialization of [HouseViewModelImpl]
 */
class HouseVmFactory(private val houseUsesCases: HouseUseCases) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HouseViewModelImpl::class.java)) {
            return HouseViewModelImpl(houseUsesCases) as T
        }
        throw IllegalArgumentException(FACTORY_EXCEPTION_MSG)
    }
}