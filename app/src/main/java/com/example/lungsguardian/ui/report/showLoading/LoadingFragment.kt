package com.example.lungsguardian.ui.report.showLoading

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.lungsguardian.R
import com.example.lungsguardian.databinding.FragmentLoadingBinding
import com.example.lungsguardian.utils.IMAGE_FILE
import com.example.lungsguardian.utils.IMAGE_URI
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class LoadingFragment : Fragment() {

    private var _binding :FragmentLoadingBinding ?= null
    private val binding get() = _binding!!
    private val loadingViewModel : LoadingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_loading, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding= FragmentLoadingBinding.bind(view)
        val uriImage = requireActivity().intent?.extras?.get(IMAGE_URI) as Uri
        val fileImage = requireActivity().intent?.extras?.get(IMAGE_FILE)
        sendImageToModel(fileImage as File)
        observers(uriImage )
    }

    private fun sendImageToModel(fileImage: File) {
        loadingViewModel.sendImageToModel(fileImage)
    }

    private fun observers(uriImage: Uri) {
        loadingViewModel.modelValidate.observe(viewLifecycleOwner){
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        loadingViewModel.responseLiveData.observe(viewLifecycleOwner){
            findNavController().navigate(
                LoadingFragmentDirections.actionLoadingFragmentToReportFragment(it.body()!!.caption,uriImage,
                ))
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}