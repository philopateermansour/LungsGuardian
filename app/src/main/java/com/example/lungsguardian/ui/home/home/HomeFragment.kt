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
import com.example.lungsguardian.utils.HOME
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
            hideLoading()
        }
        homeViewModel.responseLiveData.observe(viewLifecycleOwner){
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToReportFragment(it.body()!!.caption,uriImage!!
                ,HOME))
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

    private fun showLoading() {
        binding.cardCamera.visibility=View.GONE
        binding.cardGallery.visibility=View.GONE
        binding.imageLoading.visibility=View.VISIBLE
    }

    private fun hideLoading() {
        binding.cardCamera.visibility=View.VISIBLE
        binding.cardGallery.visibility=View.VISIBLE
        binding.imageLoading.visibility=View.GONE
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
                        fileImage=bitmapToFile(requireContext(),bitmapImage!!)
                        uriImage=bitmapToUri(requireContext(), bitmapImage!!)
                        homeViewModel.sendImageToModel(fileImage!!)
                        showLoading()
                    }
                    else{
                        uriImage = data.data
                        fileImage=uriToFile(requireContext(),uriImage!!)
                        homeViewModel.sendImageToModel(fileImage!!)
                        showLoading()
                    }

                } else {
                    Toast.makeText(activity, "Something went wrong try again", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    fun uriToFile(context: Context, uri: Uri): File? {
        var inputStream: InputStream? = null
        var outputStream: FileOutputStream? = null
        var file: File? = null

        try {
            inputStream = context.contentResolver.openInputStream(uri)
            val extension = context.contentResolver.getType(uri)?.substringAfter("/")
            val fileName = "temp_file.${extension ?: "jpg"}"
            file = File(context.cacheDir, fileName)
            outputStream = FileOutputStream(file)

            inputStream?.let {
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read: Int
                while (it.read(buffer).also { read = it } != -1) {
                    outputStream.write(buffer, 0, read)
                }
                outputStream.flush()
            }

            return file
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
            outputStream?.close()
        }

        return null
    }
    fun bitmapToUri(context: Context, bitmap: Bitmap): Uri? {
        val wrapper = ContextWrapper(context)
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)
        file = File(file, "${System.currentTimeMillis()}.jpg")

        try {
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return Uri.parse(file.absolutePath)
    }
    /*private fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
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
    }*/

    fun bitmapToFile(context: Context, bitmap: Bitmap): File? {
        // Get the directory where the file will be saved
        val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "my_images")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        // Create a file object with a unique name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "IMG_${timeStamp}.jpg"
        val file = File(directory, fileName)

        // Write the bitmap data to the file
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            return file
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            fos?.close()
        }

        return null
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}