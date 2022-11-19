package com.example.dtthouses.data.exception

import com.example.dtthouses.data.contstant.ErrorMessage.EXCEPTION_ERROR_PREFIX

/**
 * This is a custom exception used to handle network errors
 */
class NetworkException : Exception(NETWORK_ERROR_MESSAGE) {

    /**
     * Network exception constant values.
     */
    companion object {
        /**
         * Network error message when something goes wrong during a network call.
         */
        const val NETWORK_ERROR_MESSAGE =
            "$EXCEPTION_ERROR_PREFIX Could not communicate with server"
    }
}