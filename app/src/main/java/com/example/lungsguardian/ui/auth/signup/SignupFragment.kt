package com.example.lungsguardian.ui.auth.signup

import android.content.Intent
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
import com.example.lungsguardian.VALIDATE_FULL_NAME_INVALID
import com.example.lungsguardian.VALIDATE_FULL_NAME_NULL
import com.example.lungsguardian.VALIDATE_PASSWORD_CONFIGURATION_NULL
import com.example.lungsguardian.VALIDATE_PASSWORD_DOESNT_MATCH_PROBLEM
import com.example.lungsguardian.VALIDATE_PASSWORD_INVALID
import com.example.lungsguardian.VALIDATE_PASSWORD_NULL
import com.example.lungsguardian.VALIDATE_PHONE_INVALID
import com.example.lungsguardian.VALIDATE_PHONE_NULL
import com.example.lungsguardian.databinding.FragmentSignupBinding
import com.example.lungsguardian.ui.home.HomeActivity


class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private val signupViewModel: SignupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSignupBinding.bind(view)
        onClicks()
        observe()
    }

    private fun onClicks() {
        binding.login.setOnClickListener {
            findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToLoginFragment())
        }

        binding.btnSignup.setOnClickListener {
            val email = binding.editTextEmailSingUp.text.toString().trim()
            val fullName = binding.editTextFullName.text.toString().trim()
            val phone = binding.editTextPhoneNumber.text.toString().trim()
            val password = binding.editTextPasswordSignUp.text.toString()
            val confirmPassword = binding.editTextPasswordConfirm.text.toString()
            binding.progressBar.visibility= View.VISIBLE
            signupViewModel.validate(email, fullName, phone, password, confirmPassword)
        }
    }

    private fun observe() {
        signupViewModel.signUpValidate.observe(viewLifecycleOwner) {
            if (it.equals(VALIDATE_EMAIL_NULL)) {
                binding.inputTextEmailSignUp.error = getString(R.string.required)
                binding.progressBar.visibility= View.GONE
            } else if (it.equals(VALIDATE_FULL_NAME_NULL)) {
                binding.inputTextFullName.error = getString(R.string.required)
                binding.progressBar.visibility= View.GONE
            } else if (it.equals(VALIDATE_PHONE_NULL)) {
                binding.inputTextPhoneNumber.error = getString(R.string.required)
                binding.progressBar.visibility= View.GONE
            } else if (it.equals(VALIDATE_PASSWORD_NULL)) {
                binding.inputTextPasswordSignUp.error = getString(R.string.required)
                binding.progressBar.visibility= View.GONE
            } else if (it.equals(VALIDATE_PASSWORD_CONFIGURATION_NULL)) {
                binding.inputTextPasswordConfirm.error = getString(R.string.required)
                binding.progressBar.visibility= View.GONE
            } else if (it.equals(VALIDATE_PASSWORD_DOESNT_MATCH_PROBLEM)) {
                binding.inputTextPasswordConfirm.error = getString(R.string.passwords_doesn_t_match)
                binding.progressBar.visibility= View.GONE
            } else if (it.equals(VALIDATE_EMAIL_INVALID)) {
                Toast.makeText(context, VALIDATE_EMAIL_INVALID, Toast.LENGTH_SHORT).show()
                binding.inputTextEmailSignUp.isErrorEnabled = false
                binding.progressBar.visibility= View.GONE
            } else if (it.equals(VALIDATE_PHONE_INVALID)) {
                Toast.makeText(context, VALIDATE_PHONE_INVALID, Toast.LENGTH_SHORT).show()
                binding.inputTextPhoneNumber.isErrorEnabled = false
                binding.progressBar.visibility= View.GONE
            } else if (it.equals(VALIDATE_FULL_NAME_INVALID)) {
                Toast.makeText(context, VALIDATE_FULL_NAME_INVALID, Toast.LENGTH_SHORT).show()
                binding.inputTextFullName.isErrorEnabled = false
                binding.progressBar.visibility= View.GONE
            } else if (it.equals(VALIDATE_PASSWORD_INVALID)) {
                Toast.makeText(context, VALIDATE_PASSWORD_INVALID, Toast.LENGTH_LONG).show()
                binding.inputTextPasswordSignUp.isErrorEnabled = false
                binding.progressBar.visibility= View.GONE
            }
        }
        signupViewModel.responseLiveData.observe(viewLifecycleOwner) {
            if (it.code() == 200) {
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