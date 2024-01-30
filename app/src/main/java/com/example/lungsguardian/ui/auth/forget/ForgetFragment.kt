package com.example.lungsguardian.ui.auth.forget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.lungsguardian.R
import com.example.lungsguardian.VALIDATE_EMAIL_PROBLEM
import com.example.lungsguardian.databinding.FragmentForgetBinding

class ForgetFragment : Fragment() {


    private var _binding: FragmentForgetBinding ?= null
    private val binding get() = _binding!!
    private val forgetViewModel: ForgetViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forget, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentForgetBinding.bind(view)

        onClicks()
        observer()
    }

    private fun onClicks() {
        binding.btnBack.setOnClickListener {
            findNavController().navigate(ForgetFragmentDirections.actionForgetFragmentToLoginFragment())
        }
        binding.btnReset.setOnClickListener {
            val email = binding.editTextEmailReset.text.toString().trim()
            forgetViewModel.validate(email)
        }
    }

    private fun observer() {
        forgetViewModel.forgetValidate.observe(viewLifecycleOwner) {
            if (it.equals(VALIDATE_EMAIL_PROBLEM)) {
                binding.inputTextEmailReset.error = getString(R.string.required)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}