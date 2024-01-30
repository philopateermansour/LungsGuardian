package com.example.lungsguardian.ui.home.download

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lungsguardian.R
import com.example.lungsguardian.databinding.FragmentDownloadBinding


class DownloadFragment : Fragment() {


    private var _binding: FragmentDownloadBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_download, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDownloadBinding.bind(view)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}