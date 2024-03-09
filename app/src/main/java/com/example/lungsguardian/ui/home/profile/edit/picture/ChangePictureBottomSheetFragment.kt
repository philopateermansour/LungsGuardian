package com.example.lungsguardian.ui.home.profile.edit.picture

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.lungsguardian.R
import com.example.lungsguardian.databinding.BottomSheetFragmentChangePictureBinding
import com.example.lungsguardian.ui.auth.image.ChangePictureViewModel
import com.example.lungsguardian.ui.home.activity.HomeSharedViewModel
import com.example.lungsguardian.utils.CommonFunctions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class ChangePictureBottomSheetFragment:BottomSheetDialogFragment() {

    private var _binding : BottomSheetFragmentChangePictureBinding?=null
    private val binding get() = _binding!!
    private var uriImage: Uri? = null
    private var fileImage : File? = null
    private var commonFunctions= CommonFunctions()

    private val changePictureViewModel : ChangePictureViewModel by viewModels()

    private val homeSharedViewModel: HomeSharedViewModel by activityViewModels()

    companion object{
        fun getIncs()= ChangePictureBottomSheetFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_fragment_change_picture,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding= BottomSheetFragmentChangePictureBinding.bind(view)
        onClicks()
        observers()
    }


    private fun onClicks() {
        binding.imageUpload.setOnClickListener{
            openGallery()
        }
        binding.textRemove.setOnClickListener {
            changePictureViewModel.deleteProfileImage()
        }
    }

    private fun observers() {
        changePictureViewModel.errorLiveData.observe(viewLifecycleOwner){
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
        changePictureViewModel.uploadImageLiveData.observe(viewLifecycleOwner){
            Toast.makeText(context, "Profile picture changed ", Toast.LENGTH_SHORT).show()
            dismiss()
            homeSharedViewModel.isProfileChanged.value=true
        }
        changePictureViewModel.deleteImageLiveData.observe(viewLifecycleOwner){
            Toast.makeText(context, it.body(), Toast.LENGTH_SHORT).show()
            dismiss()
            homeSharedViewModel.isProfileChanged.value=true
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