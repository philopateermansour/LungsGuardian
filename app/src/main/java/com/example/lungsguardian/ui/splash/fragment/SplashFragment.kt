package com.example.lungsguardian.ui.splash.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lungsguardian.R
import com.example.lungsguardian.ui.auth.activity.AuthenticationScreen
import com.example.lungsguardian.ui.home.activity.HomeActivity
import com.example.lungsguardian.utils.LOGGED_IN
import com.example.lungsguardian.utils.LOGGED_STATE
import com.example.lungsguardian.utils.MySharedPreferences


class SplashFragment : Fragment() {




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startApp()
    }

    private fun startApp() {
        Handler(Looper.myLooper()!!).postDelayed({

            when(MySharedPreferences.getFromShared(LOGGED_STATE)){
                LOGGED_IN ->{
                    val intent = Intent(activity, HomeActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
                else ->{
                    val intent = Intent(activity, AuthenticationScreen::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }


        }, 3000)

    }


}