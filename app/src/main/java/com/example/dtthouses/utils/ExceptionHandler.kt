package com.example.dtthouses.utils

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import  com.example.dtthouses.data.contstant.ErrorMessage.EXCEPTION_ERROR_PREFIX
import kotlin.coroutines.CoroutineContext

/**
 * Handles exceptions
 */
object ExceptionHandler {
    /**
     * @param callback Callback function
     * Returns an instance of [CoroutineExceptionHandler].
     */
    fun getCoroutineExceptionHandler(callback: ((coroutineContext: CoroutineContext, throwable: Throwable) -> Unit)?): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.e(EXCEPTION_ERROR_PREFIX, throwable.message.toString())
            callback?.invoke(coroutineContext, throwable)
        }
    }
}