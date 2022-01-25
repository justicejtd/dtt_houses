package com.example.dtthouses.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dtthouses.base.ViewModelFactory.ViewModelFactoryConstants.FACTORY_EXCEPTION_MSG
import com.example.dtthouses.data.api.ServiceRepository
import com.example.dtthouses.ui.houseOverview.HouseViewModel
import java.lang.IllegalArgumentException

/**
 * Handles initialization of all view models.
 */
class ViewModelFactory(private val repository: ServiceRepository) : ViewModelProvider.Factory {
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
        if (modelClass.isAssignableFrom(HouseViewModel::class.java)) {
            return HouseViewModel(repository) as T
        }
        throw IllegalArgumentException(FACTORY_EXCEPTION_MSG)
    }
}