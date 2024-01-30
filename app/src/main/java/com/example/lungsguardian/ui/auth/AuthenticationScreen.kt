package com.example.lungsguardian.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lungsguardian.databinding.ActivityMainBinding

class AuthenticationScreen : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding?.root)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}