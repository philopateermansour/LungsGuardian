package com.example.lungsguardian.ui.auth.reset

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.lungsguardian.R
import com.example.lungsguardian.VALIDATE_CODE_INVALID
import com.example.lungsguardian.VALIDATE_CODE_NULL
import com.example.lungsguardian.VALIDATE_EMAIL_INVALID
import com.example.lungsguardian.VALIDATE_EMAIL_NULL
import com.example.lungsguardian.VALIDATE_PASSWORD_CONFIGURATION_NULL
import com.example.lungsguardian.VALIDATE_PASSWORD_DOESNT_MATCH_PROBLEM
import com.example.lungsguardian.VALIDATE_PASSWORD_INVALID
import com.example.lungsguardian.VALIDATE_PASSWORD_NULL
import com.example.lungsguardian.databinding.FragmentResetBinding
import com.example.lungsguardian.ui.home.HomeActivity


class ResetFragment : Fragment() {

    private var _binding: FragmentResetBinding? = null
    val binding get() = _binding!!

    private val resetViewModel : ResetViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentResetBinding.bind(view)

        onClicks()
        observe()

    }

    private fun onClicks() {
        binding.btnBackToSendCode.setOnClickListener{
            findNavController().navigate(ResetFragmentDirections.actionResetFragmentToForgetFragment())
        }
        binding.btnReset.setOnClickListener{
            val email = binding.editTextEmailReset.text.toString().trim()
            val code = binding.editTextCode.text.toString().trim()
            val password= binding.editTextPasswordReset.text.toString()
            val confirmPassword = binding.editTextPasswordConfirm.text.toString()
            binding.progressBar.visibility= View.VISIBLE
            resetViewModel.validate(email,code,password,confirmPassword)
        }
    }

    private fun observe() {
        resetViewModel.resetValidate.observe(viewLifecycleOwner) {
            if (it.equals(VALIDATE_EMAIL_NULL)) {
                binding.inputTextEmailReset.error = getString(R.string.required)
                binding.progressBar.visibility = View.GONE
            } else if (it.equals(VALIDATE_CODE_NULL)) {
                binding.inputTextCode.error = getString(R.string.required)
                binding.progressBar.visibility = View.GONE
            } else if (it.equals(VALIDATE_PASSWORD_NULL)) {
                binding.inputTextPasswordReset.error = getString(R.string.required)
                binding.progressBar.visibility = View.GONE
            } else if (it.equals(VALIDATE_PASSWORD_CONFIGURATION_NULL)) {
                binding.inputTextPasswordConfirm.error = getString(R.string.required)
                binding.progressBar.visibility = View.GONE
            } else if (it.equals(VALIDATE_PASSWORD_DOESNT_MATCH_PROBLEM)) {
                binding.inputTextPasswordConfirm.error = getString(R.string.passwords_doesn_t_match)
                binding.progressBar.visibility = View.GONE
            } else if (it.equals(VALIDATE_EMAIL_INVALID)) {
                Toast.makeText(context, VALIDATE_EMAIL_INVALID, Toast.LENGTH_SHORT).show()
                binding.inputTextEmailReset.isErrorEnabled = false
                binding.progressBar.visibility = View.GONE
            } else if (it.equals(VALIDATE_CODE_INVALID)) {
                Toast.makeText(context, VALIDATE_CODE_INVALID, Toast.LENGTH_SHORT).show()
                binding.inputTextCode.isErrorEnabled = false
                binding.progressBar.visibility = View.GONE
            } else if (it.equals(VALIDATE_PASSWORD_INVALID)) {
                Toast.makeText(context, VALIDATE_PASSWORD_INVALID, Toast.LENGTH_LONG).show()
                binding.inputTextPasswordReset.isErrorEnabled = false
                binding.progressBar.visibility = View.GONE
            }
        }
        resetViewModel.responseLiveData.observe(viewLifecycleOwner){
            if (it.code()==200){
                val intent = Intent(activity, HomeActivity::class.java)
                startActivity(intent)
                activity?.finish()
                binding.progressBar.visibility= View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}