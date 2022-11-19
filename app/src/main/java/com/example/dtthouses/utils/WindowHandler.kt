package com.example.dtthouses.utils

import android.os.Build
import android.os.Build.VERSION_CODES
import android.view.View
import android.view.Window
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import androidx.core.view.WindowInsetsCompat.Type.ime
import androidx.core.view.WindowInsetsCompat.toWindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Control window properties.
 */
object WindowHandler {

    /**
     * Hides window systemBars and it can be shown by swiping on top of the screen.
     * @param window [Window.javaClass]
     * @param view [View.javaClass]
     */
    fun hideWindowSystemBars(window: Window, view: View) {
        if (Build.VERSION.SDK_INT > VERSION_CODES.Q) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }
        WindowInsetsControllerCompat(window, view).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    /**
     * Hides bottom navigation when keyboard is shown.
     * @param window [Window.javaClass]
     */
    fun hideBottomNavigationWhenKeyboardIsShown(window: Window, bottomNav: BottomNavigationView) {
        window.decorView.setOnApplyWindowInsetsListener { view, insets ->
            val insetsCompat = toWindowInsetsCompat(insets, view)
            bottomNav.isGone = insetsCompat.isVisible(ime())
            view.onApplyWindowInsets(insets)
        }
    }
}