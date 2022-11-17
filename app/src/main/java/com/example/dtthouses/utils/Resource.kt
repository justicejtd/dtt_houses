package com.example.dtthouses.utils

/**
 * Keeps track of network call status.
 */
data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    /**
     * Resource static functions
     */
    companion object {

        /**
         * Return Resource with status SUCCESS.
         */
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        /**
         * Return Resource with status ERROR.
         */
        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        /**
         * Return Resource with status LOADING.
         */
        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

    }

}