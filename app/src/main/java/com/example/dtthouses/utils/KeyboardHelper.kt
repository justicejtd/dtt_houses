package com.example.dtthouses.utils

import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat

/**
 * Controls keyboard.
 */
object KeyboardHelper {
    private const val HIDE_SOFT_INPUT_FROM_WINDOW_FLAGS = 0

    /**
     * Extension function to hide soft keyboard programmatically.
     * @param root Root view.
     */
    fun hideSoftKeyboard(root: View?) {
        val imm =
            root?.let { ContextCompat.getSystemService(it.context, InputMethodManager::class.java) }
        imm?.hideSoftInputFromWindow(root.windowToken, HIDE_SOFT_INPUT_FROM_WINDOW_FLAGS)
    }
}