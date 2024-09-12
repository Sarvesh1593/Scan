package com.mack.docscan.ui.onboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.mack.docscan.MainActivity
import com.mack.docscan.databinding.ActivityOnboardBinding
import com.mack.docscan.ui.ui.onboard.onboardingAdapter

class onboard : AppCompatActivity() {
    private lateinit var adapter : onboardingAdapter
    private lateinit var viewPager: ViewPager
    private val binding : ActivityOnboardBinding by lazy {
        ActivityOnboardBinding.inflate(layoutInflater)
    }
    private val onboardingPrefs by lazy {
        OnboardingPrefs(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        viewPager = binding.viewPagerScreen
        adapter = onboardingAdapter(this)
        viewPager.adapter = adapter

        onboardingPrefs.setOnboardingCompleted()
        setupButtonListener()
    }


    private fun setupButtonListener() {
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                if (position == adapter.count) {
                    binding.btnNext.visibility = View.INVISIBLE
                    binding.indicator.visibility = View.INVISIBLE
                } else {
                    binding.btnNext.visibility = View.VISIBLE
                    binding.indicator.visibility = View.VISIBLE
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        binding.btnNext.setOnClickListener {
            if (viewPager.currentItem < adapter.count - 1) {
                viewPager.currentItem += 1
            } else {
                // Start the new activity
                val intent = Intent(this@onboard, MainActivity::class.java)
                startActivity(intent)
                finish() // Optional: if you want to finish the current activity
            }
        }
    }

}