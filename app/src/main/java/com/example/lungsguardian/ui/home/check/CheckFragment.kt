package com.example.lungsguardian.ui.home.check

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.lungsguardian.R
import com.example.lungsguardian.databinding.FragmentCheckBinding
import com.example.lungsguardian.ui.report.ReportActivity
import com.example.lungsguardian.utils.IMAGE_FILE
import com.example.lungsguardian.utils.IMAGE_URI
import java.io.File

class CheckFragment : Fragment() {

    private var _binding :FragmentCheckBinding ?=null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding=FragmentCheckBinding.bind(view)
        val uriImage = CheckFragmentArgs.fromBundle(requireArguments()).uriImage
        val fileImage =  CheckFragmentArgs.fromBundle(requireArguments()).fileImage
        setImage(uriImage)
        onClicks(uriImage,fileImage)
    }

    private fun setImage(uriImage: Uri) {
        binding.imagePicked.setImageURI(uriImage)
    }

    private fun onClicks(uriImage: Uri, fileImage: File) {
        binding.imageArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnBackToHome.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnGenerateReport.setOnClickListener {
            val intent = Intent(activity, ReportActivity::class.java)
            intent.putExtra(IMAGE_URI,uriImage)
            intent.putExtra(IMAGE_FILE,fileImage)
            startActivity(intent)
            findNavController().popBackStack()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}