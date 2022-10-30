package com.example.dtthouses.base.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dtthouses.base.factories.ViewModelFactory.ViewModelFactoryConstants.FACTORY_EXCEPTION_MSG
import com.example.dtthouses.data.api.service.house.repository.HouseRepo
import com.example.dtthouses.ui.houseOverview.viewModel.HouseViewModelImpl

/**
 * Handles initialization of all view models.
 */
class ViewModelFactory(private val repository: HouseRepo) : ViewModelProvider.Factory {
    /**
     * Constants values of view model factory.
     */
    object ViewModelFactoryConstants {
        /**
         * Exception message when view model is not found during initialization.
         */
        const val FACTORY_EXCEPTION_MSG = "Unknown ViewModel"
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HouseViewModelImpl::class.java)) {
            return HouseViewModelImpl(repository) as T
        }
        throw IllegalArgumentException(FACTORY_EXCEPTION_MSG)
    }
}