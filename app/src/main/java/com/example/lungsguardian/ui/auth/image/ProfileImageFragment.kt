package com.example.lungsguardian.ui.auth.image

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.lungsguardian.R
import com.example.lungsguardian.databinding.FragmentProfileImageBinding
import com.example.lungsguardian.ui.home.activity.HomeActivity
import com.example.lungsguardian.ui.home.profile.ProfileViewModel
import com.example.lungsguardian.utils.BASE_URL_IMAGE
import com.example.lungsguardian.utils.CommonFunctions
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class ProfileImageFragment : Fragment() {

    private var _binding :FragmentProfileImageBinding ?= null
    private val binding get() = _binding!!
    private var uriImage: Uri? = null
    private var fileImage : File? = null
    private var commonFunctions= CommonFunctions()
    private val changePictureViewModel : ChangePictureViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileImageBinding.bind(view)
        val email =ProfileImageFragmentArgs.fromBundle(requireArguments()).email
        onClicks(email)
        observers()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            findNavController().navigate(ProfileImageFragmentDirections.actionProfileImageFragmentToVerifyFragment(email))
        }
    }


    private fun onClicks(email:String) {
        binding.imageUpload.setOnClickListener {
            openGallery()
        }
        binding.textRemove.setOnClickListener {
            changePictureViewModel.deleteProfileImage()
            binding.progressBar.visibility=View.VISIBLE
        }
        binding.btnStart.setOnClickListener {
            findNavController().navigate(ProfileImageFragmentDirections.actionProfileImageFragmentToVerifyFragment(email))
        }
    }
    private fun observers() {
        changePictureViewModel.errorLiveData.observe(viewLifecycleOwner){
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            binding.progressBar.visibility=View.GONE
        }
        changePictureViewModel.uploadImageLiveData.observe(viewLifecycleOwner){
            Toast.makeText(context, "Profile picture changed ", Toast.LENGTH_SHORT).show()
            Glide.with(binding.root.context).load("$BASE_URL_IMAGE${it.body()?.uploadedImage}")
                .into(binding.profilePic)
            binding.progressBar.visibility=View.GONE

        }
        changePictureViewModel.deleteImageLiveData.observe(viewLifecycleOwner){
            Toast.makeText(context, it.body(), Toast.LENGTH_SHORT).show()
            binding.progressBar.visibility=View.GONE
            binding.profilePic.setImageResource(R.drawable.user_profile)
        }
    }
    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        try {
            resultLauncher.launch(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, "Something went wrong try again", Toast.LENGTH_SHORT).show()
        }
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

            try {
                if (it.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = it.data
                    uriImage = data?.data
                    fileImage=commonFunctions.uriToFile(requireContext(),uriImage!!)
                    changePictureViewModel.uploadProfileImage(fileImage!!)
                    binding.progressBar.visibility=View.VISIBLE
                }} catch (e: Exception){
                Toast.makeText(activity, "${e.localizedMessage}, try again", Toast.LENGTH_SHORT)
                    .show()
            }
        }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}