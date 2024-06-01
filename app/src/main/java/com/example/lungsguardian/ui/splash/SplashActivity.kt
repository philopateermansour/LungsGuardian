package com.example.lungsguardian.ui.splash

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.lungsguardian.R
import com.example.lungsguardian.ui.auth.activity.AuthenticationScreen
import com.example.lungsguardian.ui.home.activity.HomeActivity
import com.example.lungsguardian.utils.LOGGED_IN
import com.example.lungsguardian.utils.LOGGED_STATE
import com.example.lungsguardian.utils.MySharedPreferences

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        startApp()
        this.requestedOrientation= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
    private fun startApp() {
        Handler(Looper.myLooper()!!).postDelayed({

            when(MySharedPreferences.getFromShared(LOGGED_STATE)){
                LOGGED_IN ->{
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    this.finish()
                }
                else ->{
                    val intent = Intent(this, AuthenticationScreen::class.java)
                    startActivity(intent)
                    this.finish()
                }
            }
        }, 3000)
    }
}