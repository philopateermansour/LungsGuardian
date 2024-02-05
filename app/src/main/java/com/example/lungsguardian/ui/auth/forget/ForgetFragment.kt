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
    ): View {
        _binding=FragmentForgetBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClicks()
        observer()
    }

    private fun onClicks() {
        binding.btnBack.setOnClickListener {
            findNavController().navigate(ForgetFragmentDirections.actionForgetFragmentToLoginFragment())
        }
        binding.btnSendCode.setOnClickListener {
            val email = binding.editTextEmailReset.text.toString().trim()
            binding.progressBar.visibility= View.VISIBLE
            binding.btnSendCode.text=null
            forgetViewModel.validate(email)
        }
    }

    private fun observer() {
        forgetViewModel.forgetValidate.observe(viewLifecycleOwner) {
            if (it.equals(VALIDATE_EMAIL_NULL)) {
                binding.inputTextEmailReset.error = getString(R.string.required)
                binding.progressBar.visibility= View.GONE
                binding.btnSendCode.setText(R.string.send_code)
            } else if (it.equals(VALIDATE_EMAIL_INVALID)) {
                binding.inputTextEmailReset.isErrorEnabled = false
                Toast.makeText(context, VALIDATE_EMAIL_INVALID, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility= View.GONE
                binding.btnSendCode.setText(R.string.send_code)
            }
        }
        forgetViewModel.sendCodeResponse.observe(viewLifecycleOwner){
            if (it.code() == 200){
                findNavController().navigate(ForgetFragmentDirections.actionForgetFragmentToResetFragment())
                binding.progressBar.visibility= View.GONE
                binding.btnSendCode.setText(R.string.send_code)
            }
            else if (it.code() == 400){
                Toast.makeText(context,"this email is not registered in the system",Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility= View.GONE
                binding.btnSendCode.setText(R.string.send_code)
                binding.inputTextEmailReset.isErrorEnabled = false
            }
            else{
                Toast.makeText(context,"error",Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility= View.GONE
                binding.btnSendCode.setText(R.string.send_code)
                binding.inputTextEmailReset.isErrorEnabled = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}