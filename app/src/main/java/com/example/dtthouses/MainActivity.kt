package com.example.dtthouses

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        val host: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerViewHost) as NavHostFragment
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // Setup navigation graph controller to bottom navigation
        bottomNav?.setupWithNavController(host.navController)
    }
}