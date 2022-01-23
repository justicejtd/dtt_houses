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


class MainActivity : AppCompatActivity(), AboutFragmentCallback {
    private val requestCode = 100
    private lateinit var hostFragment: NavHostFragment

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

            if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                // Notify user that permission has been granted
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show()

                val houseFragment = hostFragment.childFragmentManager.primaryNavigationFragment
                if (houseFragment is HouseFragment) {
                    // If permission is granted started getting user location
                    houseFragment.getCurrentLocation()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun showBrowser() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.d-tt.nl/"))
        startActivity(intent)
    }
}