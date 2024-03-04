package com.example.lungsguardian.ui.home.report

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.example.lungsguardian.R
import com.example.lungsguardian.databinding.FragmentReportBinding
import java.io.File


class ReportFragment : Fragment() {
    private var _binding :FragmentReportBinding ?=null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding=FragmentReportBinding.bind(view)
        val receivedImage = ReportFragmentArgs.fromBundle(requireArguments()).image
        val receivedCaption = ReportFragmentArgs.fromBundle(requireArguments()).caption
        showReport(receivedImage,receivedCaption)
        onClicks()
    }

    private fun onClicks() {
        binding.imageArrow.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

    private fun showReport(receivedImage: Uri, receivedCaption: String) {

        binding.imageXray.setImageURI(receivedImage)
        binding.caption.text = receivedCaption
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}