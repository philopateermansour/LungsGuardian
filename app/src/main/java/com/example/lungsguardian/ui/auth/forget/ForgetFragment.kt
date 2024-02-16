package com.example.lungsguardian.ui.auth.forget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.lungsguardian.R
import com.example.lungsguardian.databinding.FragmentForgetBinding
import com.example.lungsguardian.utils.SEND_CODE_DONE
import com.example.lungsguardian.utils.SEND_CODE_Failed
import com.example.lungsguardian.utils.VALIDATE_EMAIL_INVALID
import com.example.lungsguardian.utils.VALIDATE_EMAIL_NULL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        observers()
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
        binding.editTextEmailReset.doAfterTextChanged {
            binding.inputTextEmailReset.error=""
        }
    }

    private fun observers() {
        forgetViewModel.forgetValidate.observe(viewLifecycleOwner) {
            if (it.equals(VALIDATE_EMAIL_NULL)) {
                binding.inputTextEmailReset.error = getString(R.string.required)
                binding.progressBar.visibility= View.GONE
                binding.btnSendCode.setText(R.string.send_code)
            } else if (it.equals(VALIDATE_EMAIL_INVALID)) {
                Toast.makeText(context, VALIDATE_EMAIL_INVALID, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility= View.GONE
                binding.btnSendCode.setText(R.string.send_code)
            } else{
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility= View.GONE
                binding.btnSendCode.setText(R.string.send_code)
            }
        }
        forgetViewModel.sendCodeResponse.observe(viewLifecycleOwner){
            if (it.code()==200){
                findNavController().navigate(ForgetFragmentDirections.actionForgetFragmentToResetFragment(
                    binding.editTextEmailReset.text.toString()
                ))

                Toast.makeText(context, SEND_CODE_DONE,Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility= View.GONE
                binding.btnSendCode.setText(R.string.send_code)
            }
            else {
                Toast.makeText(context, SEND_CODE_Failed,Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility= View.GONE
                binding.btnSendCode.setText(R.string.send_code)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}