package com.example.dtthouses.ui.house

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.dtthouses.ui.about.AboutFragmentCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.core.view.WindowInsetsCompat.Type.ime
import androidx.core.view.WindowInsetsCompat.toWindowInsetsCompat
import androidx.core.view.isGone
import com.example.dtthouses.databinding.ActivityHomeBinding
import com.example.dtthouses.ui.house.HomeActivity.HouseActivityConstants.DTT_URL
import com.example.dtthouses.utils.LocationProvider.LOCATION_REQUEST_CODE
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity for handling bottom navigation fragments.
 */
@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), AboutFragmentCallback {
    private lateinit var binding: ActivityHomeBinding
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
        // Setup view binding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize views
        setupViews()

        // Set status bar color to gray
        setStatusBarColor()

        // Hide bottom navigation when keyboard is shown
        hideBottomNavigation()
    }

    private fun setupViews() {
        hostFragment = binding.fcvHost.getFragment()
        bottomNav = binding.bottomNavigationView
        bottomNav.setupWithNavController(hostFragment.navController)
    }

    private fun setStatusBarColor() {
        this.window.statusBarColor = Color.GRAY
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // Check if its the location request
        if (requestCode == LOCATION_REQUEST_CODE) {

            val houseFragment = hostFragment.childFragmentManager.primaryNavigationFragment
            if (houseFragment is HouseFragment) {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
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