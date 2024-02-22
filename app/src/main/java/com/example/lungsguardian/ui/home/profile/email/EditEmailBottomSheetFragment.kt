package com.example.lungsguardian.ui.home.profile.email

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lungsguardian.R
import com.example.lungsguardian.databinding.BottomSheetFragmentEditEmailBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditEmailBottomSheetFragment :BottomSheetDialogFragment() {
    private var _binding :BottomSheetFragmentEditEmailBinding ?=null
    private val binding get() = _binding!!
    companion object{
        fun getIncs()=EditEmailBottomSheetFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_fragment_edit_email, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = BottomSheetFragmentEditEmailBinding.bind(view)
        onclicks()
    }

    private fun onclicks() {
        binding.btnResetEmail.setOnClickListener {
            val email = binding.textEditEmail.text

        }
    }
}