package com.example.lungsguardian.ui.home.report

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lungsguardian.R
import com.example.lungsguardian.databinding.FragmentReportBinding
import com.example.lungsguardian.utils.HISTORY
import com.example.lungsguardian.utils.HOME


class ReportFragment : Fragment() {
    private var _binding: FragmentReportBinding? = null
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
        _binding = FragmentReportBinding.bind(view)
        val receivedImage = ReportFragmentArgs.fromBundle(requireArguments()).image
        val receivedCaption = ReportFragmentArgs.fromBundle(requireArguments()).caption
        val receivedSource = ReportFragmentArgs.fromBundle(requireArguments()).source
        showReport(receivedImage, receivedCaption)

        onClicks(receivedSource)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navigateBack(receivedSource)
        }
        }

    private fun onClicks(receivedSource: String) {
        binding.imageArrow.setOnClickListener {
            navigateBack(receivedSource)
        }
    }

    private fun navigateBack(receivedSource: String) {
        if (receivedSource == HOME) {

            findNavController().navigate(ReportFragmentDirections.actionReportFragmentToHomeFragment2())
        } else if (receivedSource == HISTORY) {
            findNavController().navigate(ReportFragmentDirections.actionReportFragmentToHistoryFragment())
        }
    }

    private fun showReport(receivedImage: Uri, receivedCaption: String) {
        binding.imageXray.setImageURI(receivedImage)
        binding.caption.text = receivedCaption
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}