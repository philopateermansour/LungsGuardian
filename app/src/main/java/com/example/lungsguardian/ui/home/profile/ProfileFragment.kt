package com.example.lungsguardian.ui.home.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.lungsguardian.databinding.FragmentProfileBinding
import com.example.lungsguardian.ui.auth.activity.AuthenticationScreen
import com.example.lungsguardian.utils.MySharedPreferences
import com.example.lungsguardian.utils.USER_EMAIL
import com.example.lungsguardian.utils.USER_NAME

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel  : ProfileViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClicks()
        updateUserData()
    }

    private fun updateUserData() {
        binding.fullName.text=MySharedPreferences.getFromShared(USER_NAME)
        binding.email.text=MySharedPreferences.getFromShared(USER_EMAIL)
    }

    private fun onClicks() {
        binding.logoutCardView.setOnClickListener {
            profileViewModel.clearUserData()
            val intent = Intent(activity, AuthenticationScreen::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}