package com.example.dtthouses.base

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.dtthouses.R
import com.example.dtthouses.ui.about.AboutFragmentCallback
import com.example.dtthouses.ui.houseOverview.HouseFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import com.example.dtthouses.base.HouseActivity.HouseActivityConstants.DTT_LINK
import com.example.dtthouses.base.HouseActivity.HouseActivityConstants.TOAST_PERMISSION_GRANTED

class HouseActivity : AppCompatActivity(), AboutFragmentCallback {
    private val requestCode = 100
    private lateinit var hostFragment: NavHostFragment

    object HouseActivityConstants {
        const val TOAST_PERMISSION_GRANTED = "Permission Granted"
        const val DTT_LINK = "https://www.d-tt.nl/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        hostFragment = supportFragmentManager.findFragmentById(R.id.fcvHost) as NavHostFragment
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // Setup navigation graph controller to bottom navigation
        bottomNav?.setupWithNavController(hostFragment.navController)

        // Set status bar color to gray
        this.window.statusBarColor = Color.GRAY
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
                    Toast.makeText(this, TOAST_PERMISSION_GRANTED, Toast.LENGTH_LONG).show()

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
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(DTT_LINK))
        startActivity(intent)
    }
}