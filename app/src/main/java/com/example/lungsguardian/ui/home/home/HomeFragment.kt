package com.example.lungsguardian.ui.home.home

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var fileImage: Bitmap? = null
    private val homeViewModel :HomeViewModel by viewModels()
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
        observers()
    }

    private fun observers() {
        homeViewModel.modelValidate.observe(viewLifecycleOwner){
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
        homeViewModel.responseLiveData.observe(viewLifecycleOwner){
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToReportFragment(it.body()!!.caption,fileImage!!))
        }
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
                    fileImage = data.extras?.getParcelable("data",) as Bitmap?

                    }
                    else{
                        fileImage = context?.let { it1 -> uriToBitmap(it1,data.data!!) }
                    }
                    homeViewModel.sendImageToModel(fileImage!!)
                } else {
                    Toast.makeText(activity, "Something went wrong try again", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    private fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
        var inputStream: InputStream? = null
        try {
            // Open an input stream from the URI
            inputStream = context.contentResolver.openInputStream(uri)

            // Decode the input stream into a Bitmap
            return BitmapFactory.decodeStream(inputStream)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } finally {
            try {
                // Close the input stream
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }
    fun convertBitmapToPNG(bitmap: Bitmap) {
        // Create a file to save the PNG image
        val file = File(Environment.getExternalStorageDirectory(), "converted_image.png")

        try {
            // Create a file output stream
            val fos = FileOutputStream(file)

            // Compress the bitmap to PNG format and write to the file output stream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)

            // Close the file output stream
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}