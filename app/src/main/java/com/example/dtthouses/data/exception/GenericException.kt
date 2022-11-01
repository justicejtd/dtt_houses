package com.example.dtthouses.data.exception

import com.example.dtthouses.data.contstant.ErrorMessage.EXCEPTION_ERROR_PREFIX

/**
 * This is a generic exception used to handle unexpected errors
 */
class GenericException : Exception(GENERIC_ERROR_MESSAGE) {

    /**
     * Generic exception constant values.
     */
    companion object {
        /**
         * Generic error message when something goes wrong.
         */
        private const val GENERIC_ERROR_MESSAGE = "$EXCEPTION_ERROR_PREFIX An unexpected error has occurred, please contact DTT support"
    }
}