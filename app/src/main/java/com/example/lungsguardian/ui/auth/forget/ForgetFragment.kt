package com.example.lungsguardian.ui.auth.forget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.lungsguardian.R
import com.example.lungsguardian.VALIDATE_EMAIL_INVALID
import com.example.lungsguardian.VALIDATE_EMAIL_NULL
import com.example.lungsguardian.databinding.FragmentForgetBinding

class ForgetFragment : Fragment() {


    private var _binding: FragmentForgetBinding? = null
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
            binding.progressBar.visibility= View.VISIBLE
            forgetViewModel.validate(email)
        }
    }

    private fun observer() {
        forgetViewModel.forgetValidate.observe(viewLifecycleOwner) {
            if (it.equals(VALIDATE_EMAIL_NULL)) {
                binding.inputTextEmailReset.error = getString(R.string.required)
                binding.progressBar.visibility= View.GONE
            } else if (it.equals(VALIDATE_EMAIL_INVALID)) {
                binding.inputTextEmailReset.isErrorEnabled = false
                Toast.makeText(context, VALIDATE_EMAIL_INVALID, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility= View.GONE
            }
        }
        forgetViewModel.sendCodeResponse.observe(viewLifecycleOwner){
            if (it.code() == 200){
                findNavController().navigate(ForgetFragmentDirections.actionForgetFragmentToResetFragment())
                binding.progressBar.visibility= View.GONE
            }
            else if (it.code() == 400){
                Toast.makeText(context,"this email is not registered in the system",Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility= View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}