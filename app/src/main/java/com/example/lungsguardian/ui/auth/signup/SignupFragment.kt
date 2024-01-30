package com.example.lungsguardian.ui.auth.signup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.lungsguardian.R
import com.example.lungsguardian.VALIDATE_EMAIL_PROBLEM
import com.example.lungsguardian.VALIDATE_FULLNAME_PROBLEM
import com.example.lungsguardian.VALIDATE_PASSWORDCONFIGURATION_PROBLEM
import com.example.lungsguardian.VALIDATE_PASSWORDDOESNTMATCH_PROBLEM
import com.example.lungsguardian.VALIDATE_PASSWORD_PROBLEM
import com.example.lungsguardian.VALIDATE_PHONE_PROBLEM
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
            signupViewModel.validate(email, fullName, phone, password, confirmPassword)
        }
    }

    private fun observe() {
        signupViewModel.signUpValidate.observe(viewLifecycleOwner) {
            if (it.equals(VALIDATE_EMAIL_PROBLEM)) {
                binding.inputTextEmailSignUp.error =  getString(R.string.required)
            } else if (it.equals(VALIDATE_FULLNAME_PROBLEM)) {
                binding.inputTextFullName.error =  getString(R.string.required)
            } else if (it.equals(VALIDATE_PHONE_PROBLEM)) {
                binding.inputTextPhoneNumber.error =  getString(R.string.required)
            } else if (it.equals(VALIDATE_PASSWORD_PROBLEM)) {
                binding.inputTextPasswordSignUp.error =  getString(R.string.required)
            } else if (it.equals(VALIDATE_PASSWORDCONFIGURATION_PROBLEM)) {
                binding.inputTextPasswordConfirm.error =  getString(R.string.required)
            } else if (it.equals(VALIDATE_PASSWORDDOESNTMATCH_PROBLEM)) {
                binding.inputTextPasswordConfirm.error = getString(R.string.passwords_doesn_t_match)
            }
        }
        signupViewModel.responseLiveData.observe(viewLifecycleOwner){
            if (it.isSuccessful){
                val intent = Intent (activity, HomeActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}