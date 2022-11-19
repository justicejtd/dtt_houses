package com.example.dtthouses.ui.house

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.dtthouses.databinding.ActivityHomeBinding
import com.example.dtthouses.utils.LocationProvider.LOCATION_REQUEST_CODE
import com.example.dtthouses.utils.WindowHandler
import dagger.hilt.android.AndroidEntryPoint


/**
 * Activity for handling bottom navigation fragments.
 */
@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var hostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()
        setupWindow() // Setup statusBar and bottom Navigation bar.
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
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

    private fun setupViews() {
        hostFragment = binding.fcvHost.getFragment()
        binding.bottomNavigationView.setupWithNavController(hostFragment.navController)
    }

    private fun setupWindow() {
        WindowHandler.hideWindowSystemBars(window, binding.root)
        WindowHandler.hideBottomNavigationWhenKeyboardIsShown(window, binding.bottomNavigationView)
    }
}