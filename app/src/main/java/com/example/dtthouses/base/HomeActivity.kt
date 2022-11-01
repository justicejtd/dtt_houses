package com.example.dtthouses.base

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.dtthouses.R
import com.example.dtthouses.ui.about.AboutFragmentCallback
import com.example.dtthouses.ui.house.HouseFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.core.view.WindowInsetsCompat.Type.ime
import androidx.core.view.WindowInsetsCompat.toWindowInsetsCompat
import androidx.core.view.isGone
import com.example.dtthouses.base.HomeActivity.HouseActivityConstants.DTT_URL

/**
 * Activity for handling bottom navigation fragments.
 */
class HomeActivity : AppCompatActivity(), AboutFragmentCallback {
    private val requestCode = 100
    private lateinit var hostFragment: NavHostFragment
    private lateinit var bottomNav: BottomNavigationView

    /**
     * Constant values of house activity.
     */
    object HouseActivityConstants {
        /**
         * URL link of DTT main website.
         */
        const val DTT_URL = "https://www.d-tt.nl/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize views
        hostFragment = supportFragmentManager.findFragmentById(R.id.fcvHost) as NavHostFragment
        bottomNav = findViewById(R.id.bottomNavigationView)

        // Setup navigation graph controller to bottom navigation
        bottomNav.setupWithNavController(hostFragment.navController)

        // Set status bar color to gray
        this.window.statusBarColor = Color.GRAY

        // Hide bottom navigation when keyboard is shown
        hideBottomNavigation()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // Check if its the location request
        if (requestCode == this.requestCode) {

            val houseFragment = hostFragment.childFragmentManager.primaryNavigationFragment
            if (houseFragment is HouseFragment) {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // Notify user that permission has been granted
                    Toast.makeText(
                        this,
                        getString(R.string.permission_granted_msg),
                        Toast.LENGTH_LONG
                    ).show()

                    // If permission is granted started getting user location
                    houseFragment.getCurrentLocation()

                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    // Hide location views
                    houseFragment.handlePermissionDenied()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun showBrowser() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(DTT_URL))
        startActivity(intent)
    }

    private fun hideBottomNavigation() {
        window.decorView.setOnApplyWindowInsetsListener { view, insets ->
            val insetsCompat = toWindowInsetsCompat(insets, view)
            bottomNav.isGone = insetsCompat.isVisible(ime())
            view.onApplyWindowInsets(insets)
        }
    }
}