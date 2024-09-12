package com.mack.docscan


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.mack.docscan.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var navController : NavController
    private var binding : ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navControl) as?
                NavHostFragment
        navHostFragment?.let{
            navController = it.findNavController()
        }
        

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}