package com.example.dtthouses.utils

import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat

/**
 * Controls keyboard.
 */
object KeyboardHelper {

    /**
     * Extension function to hide soft keyboard programmatically.
     * @param root Root view.
     */
    fun hideSoftKeyboard(root: View?) {
        val imm =
            root?.let { ContextCompat.getSystemService(it.context, InputMethodManager::class.java) }
        imm?.hideSoftInputFromWindow(root.windowToken, 0)
    }
}