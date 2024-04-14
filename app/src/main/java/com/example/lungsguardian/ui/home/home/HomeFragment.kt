package com.example.lungsguardian.ui.home.home

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.lungsguardian.databinding.FragmentHomeBinding
import com.example.lungsguardian.ui.report.ReportActivity
import com.example.lungsguardian.ui.report.showLoading.LoadingViewModel
import com.example.lungsguardian.utils.CommonFunctions
import com.example.lungsguardian.utils.HOME
import com.example.lungsguardian.utils.IMAGE_FILE
import com.example.lungsguardian.utils.IMAGE_URI
import com.example.lungsguardian.utils.SOURCE
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var uriImage: Uri? = null
    private var bitmapImage: Bitmap? = null
    private var fileImage :File? = null
    private var commonFunctions=CommonFunctions()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClicks()
    }


    private fun onClicks() {
        binding.cardGallery.setOnClickListener {
            openGallery()
        }
        binding.cardCamera.setOnClickListener {
            captureByCamera()
        }
    }



    private fun captureByCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            resultLauncher.launch(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, "Something went wrong try again", Toast.LENGTH_SHORT).show()
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
            if (it.resultCode == Activity.RESULT_OK) {
                val data: Intent? = it.data
                if (data != null) {
                    if (data.hasExtra("data")){
                        bitmapImage=(data.extras?.getParcelable("data",) as Bitmap?)!!
                        fileImage=commonFunctions.bitmapToFile(requireContext(),bitmapImage!!)
                        uriImage=commonFunctions.bitmapToUri(requireContext(), bitmapImage!!)
                        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCheckFragment(uriImage!!,fileImage!!))
                    }
                    else{
                        uriImage = data.data
                        fileImage=commonFunctions.uriToFile(requireContext(),uriImage!!)
                        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCheckFragment(uriImage!!,fileImage!!))
                    }
                } else {
                    Toast.makeText(activity, "Something went wrong try again", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}