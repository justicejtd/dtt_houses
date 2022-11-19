package com.example.dtthouses.ui.house

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsCompat.Type.ime
import androidx.core.view.WindowInsetsCompat.toWindowInsetsCompat
import androidx.core.view.isGone
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.dtthouses.databinding.ActivityHomeBinding
import com.example.dtthouses.utils.LocationProvider.LOCATION_REQUEST_CODE
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


/**
 * Activity for handling bottom navigation fragments.
 */
@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var hostFragment: NavHostFragment
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        installSplashScreen()
        super.onCreate(savedInstanceState)

        // Setup view binding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()

        // Hide bottom navigation when keyboard is shown
        hideBottomNavigation()
    }

    private fun setupViews() {
        hostFragment = binding.fcvHost.getFragment()
        bottomNav = binding.bottomNavigationView
        bottomNav.setupWithNavController(hostFragment.navController)
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

    private fun hideBottomNavigation() {
        window.decorView.setOnApplyWindowInsetsListener { view, insets ->
            val insetsCompat = toWindowInsetsCompat(insets, view)
            bottomNav.isGone = insetsCompat.isVisible(ime())
            view.onApplyWindowInsets(insets)
        }
    }
}