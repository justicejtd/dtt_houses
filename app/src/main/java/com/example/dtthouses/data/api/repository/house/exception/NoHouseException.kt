package com.example.dtthouses.data.api.repository.house.exception

import com.example.dtthouses.data.contstant.ErrorMessage.EXCEPTION_ERROR_PREFIX

/**
 * Exception class used when no houses is found during network call.
 */
class NoHouseException: Exception(NO_HOUSES_FOUND_ERROR) {
    /**
     * No houses found exception constant values.
     */
    companion object {
        /**
         * Error message when no houses are found during network call.
         */
        private const val NO_HOUSES_FOUND_ERROR = "$EXCEPTION_ERROR_PREFIX No houses found, please try again!"
    }
}