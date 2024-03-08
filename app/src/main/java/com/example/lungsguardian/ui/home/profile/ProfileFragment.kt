package com.example.lungsguardian.ui.home.profile

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.lungsguardian.R
import com.example.lungsguardian.databinding.FragmentProfileBinding
import com.example.lungsguardian.ui.auth.activity.AuthenticationScreen
import com.example.lungsguardian.ui.home.activity.HomeSharedViewModel
import com.example.lungsguardian.ui.home.profile.edit.email.EditEmailBottomSheetFragment
import com.example.lungsguardian.ui.home.profile.edit.name.EditNameBottomSheetFragment
import com.example.lungsguardian.ui.home.profile.edit.password.ChangePasswordBottomSheetFragment
import com.example.lungsguardian.ui.home.profile.edit.picture.ChangePictureBottomSheetFragment
import com.example.lungsguardian.ui.report.ReportActivity
import com.example.lungsguardian.utils.BASE_URL_IMAGE
import com.example.lungsguardian.utils.CommonFunctions
import com.example.lungsguardian.utils.IMAGE_FILE
import com.example.lungsguardian.utils.IMAGE_URI
import com.example.lungsguardian.utils.MySharedPreferences
import com.example.lungsguardian.utils.TRUE
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.wait
import java.io.File
import java.lang.Exception

@AndroidEntryPoint
class ProfileFragment : Fragment()  {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModels()
    private val homeSharedViewModel: HomeSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClicks()
        updateUserData()
        observers()
    }



    private fun updateUserData() {
        profileViewModel.showProfile()
    }

    private  fun onClicks() {
        binding.emailCardView.setOnClickListener {
             activity?.let { it1 ->
                EditEmailBottomSheetFragment.getIncs().show(it1.supportFragmentManager, "TAG")
            }
        }
        binding.passwordCardView.setOnClickListener {
            activity?.let { it1 ->
                ChangePasswordBottomSheetFragment.getIncs().show(it1.supportFragmentManager, "TAG")
            }
        }
        binding.fullNameCardView.setOnClickListener {
            activity?.let { it1 ->
                EditNameBottomSheetFragment.getIncs().show(it1.supportFragmentManager, "TAG")
            }
        }
        binding.profilePic.setOnClickListener {
            activity?.let { it1 ->
                ChangePictureBottomSheetFragment.getIncs().show(it1.supportFragmentManager, "TAG")
            }
        }
        binding.logoutCardView.setOnClickListener {
            MySharedPreferences.clearShared()
            val intent = Intent(activity, AuthenticationScreen::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }



    private fun observers() {
        profileViewModel.profileLiveData.observe(viewLifecycleOwner) {
            if (it.code() == 200) {
                binding.email.text = it.body()?.email
                binding.fullName.text = it.body()?.fullName
                if (it.body()?.pictureUrl.isNullOrEmpty()){
                    binding.profilePic.setImageResource(R.drawable.user_profile)
                }
                else{
                Glide.with(binding.root.context).load("$BASE_URL_IMAGE${it.body()?.pictureUrl}")
                    .into(binding.profilePic)}
            } else {
                Toast.makeText(context, it.message(), Toast.LENGTH_SHORT).show()
            }
        }
        profileViewModel.errorLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
        homeSharedViewModel.isProfileChanged.observe(viewLifecycleOwner){
            if (it==true){
                updateUserData()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}