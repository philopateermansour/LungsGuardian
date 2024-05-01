package com.example.lungsguardian.ui.report.showReport

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lungsguardian.R
import com.example.lungsguardian.databinding.FragmentReportBinding
import com.example.lungsguardian.utils.ARABIC
import com.example.lungsguardian.utils.BACK_TO_ENGLISH
import com.example.lungsguardian.utils.GO_TO_ARABIC
import java.util.Locale


class ReportFragment : Fragment() {
    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!
    lateinit var language :String
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
        val arabicCaption = ReportFragmentArgs.fromBundle(requireArguments()).arabicCaption
        checkLanguage()
        showTranslateButton()
        showReport(receivedImage, receivedCaption)
        onClicks(receivedCaption,arabicCaption)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navigateBack()
        }
    }

    private fun showTranslateButton() {
        if (language== ARABIC){
            binding.btnTranslate.visibility=View.VISIBLE
        }
        else{
            binding.btnTranslate.visibility=View.GONE
        }
    }


    private fun onClicks(receivedCaption: String,arabicCaption:String) {
        binding.imageArrow.setOnClickListener {
            navigateBack()
        }
        binding.btnTranslate.setOnClickListener {
            if (binding.btnTranslate.text== GO_TO_ARABIC){
            binding.caption.text=arabicCaption
            binding.btnTranslate.text= BACK_TO_ENGLISH
            }
            else if ( binding.btnTranslate.text == BACK_TO_ENGLISH){
                binding.caption.text=receivedCaption
                binding.btnTranslate.text=GO_TO_ARABIC
            }
        }
    }
    private fun navigateBack() {
        requireActivity().finish()
    }

    private fun checkLanguage() {
        val currentLocale: Locale = Locale.getDefault()
        language = currentLocale.language
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