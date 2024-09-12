package com.mack.docscan

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mack.docscan.ui.onboard.OnboardingPrefs
import com.mack.docscan.ui.onboard.onboard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private val onboardingPrefs by lazy {
        OnboardingPrefs(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

        lifecycleScope.launch {
            delay(1000)
            if (onboardingPrefs.isOnboardingCompleted()) {
                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            } else {
                startActivity(Intent(this@SplashScreen, onboard::class.java))
            }
            finish()
        }
    }

}