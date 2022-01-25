package com.example.dtthouses.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.dtthouses.R
import com.example.dtthouses.base.HouseActivity
import com.example.dtthouses.ui.splash.SplashActivity.SplashActivityConstants.SPLASH_ALPHA
import com.example.dtthouses.ui.splash.SplashActivity.SplashActivityConstants.SPLASH_DURATION


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    object SplashActivityConstants {
        const val SPLASH_DURATION: Long = 1
        const val SPLASH_ALPHA = 1f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Initialize image logo view
        val ivLogo = findViewById<ImageView>(R.id.ivLogo)

        // Show splash screen for 1 seconds
        ivLogo.animate().setDuration(SPLASH_DURATION).alpha(SPLASH_ALPHA).withEndAction {
            // Show Splash screen until main screen is ready
            val intent = Intent(this, HouseActivity::class.java)
            startActivity(intent)

            // Set fade in and out animation
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            // Close splash screen
            finish()
        }
    }
}