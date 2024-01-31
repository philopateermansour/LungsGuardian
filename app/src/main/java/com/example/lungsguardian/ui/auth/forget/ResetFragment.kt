package com.example.lungsguardian.ui.auth.forget

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lungsguardian.R
import com.example.lungsguardian.databinding.FragmentResetBinding


class ResetFragment : Fragment() {

    private var _binding:FragmentResetBinding ?=null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding =FragmentResetBinding.bind(view)
    }


}