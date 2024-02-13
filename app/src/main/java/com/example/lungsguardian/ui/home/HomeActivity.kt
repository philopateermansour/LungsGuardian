package com.example.lungsguardian.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.lungsguardian.R.id.fragmentContainerView
import com.example.lungsguardian.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private var _binding: ActivityHomeBinding? = null
    val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val navHostFragment = supportFragmentManager.findFragmentById(fragmentContainerView)
                as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigator.setupWithNavController(navController)


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}