package com.example.lungsguardian.ui.auth.signup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.lungsguardian.R
import com.example.lungsguardian.data.model.UserSignupModel
import com.example.lungsguardian.utils.VALIDATE_EMAIL_INVALID
import com.example.lungsguardian.utils.VALIDATE_EMAIL_NULL
import com.example.lungsguardian.utils.VALIDATE_FULL_NAME_INVALID
import com.example.lungsguardian.utils.VALIDATE_FULL_NAME_NULL
import com.example.lungsguardian.utils.VALIDATE_PASSWORD_CONFIGURATION_NULL
import com.example.lungsguardian.utils.VALIDATE_PASSWORD_DOESNT_MATCH_PROBLEM
import com.example.lungsguardian.utils.VALIDATE_PASSWORD_INVALID
import com.example.lungsguardian.utils.VALIDATE_PASSWORD_NULL
import com.example.lungsguardian.utils.VALIDATE_PHONE_INVALID
import com.example.lungsguardian.utils.VALIDATE_PHONE_NULL
import com.example.lungsguardian.databinding.FragmentSignupBinding
import com.example.lungsguardian.ui.home.activity.HomeActivity
import com.example.lungsguardian.utils.EMAIL_REGISTERED
import com.example.lungsguardian.utils.FALSE
import com.example.lungsguardian.utils.TRUE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private val signupViewModel: SignupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignupBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClicks()
        observers()
    }

    private fun onClicks() {
        binding.login.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSignup.setOnClickListener {
            val email = binding.editTextEmailSingUp.text.toString().trim()
            val fullName = binding.editTextFullName.text.toString().trim()
            val phone = binding.editTextPhoneNumber.text.toString().trim()
            val password = binding.editTextPasswordSignUp.text.toString()
            val confirmPassword = binding.editTextPasswordConfirm.text.toString()
            binding.progressBar.visibility= View.VISIBLE
            binding.btnSignup.text = null
            signupViewModel.validate(email, fullName, phone, password, confirmPassword)
        }
        binding.editTextEmailSingUp.doAfterTextChanged {
            binding.inputTextEmailSignUp.error=""
        }
        binding.editTextFullName.doAfterTextChanged {
            binding.inputTextFullName.error=""
        }
        binding.editTextPhoneNumber.doAfterTextChanged {
            binding.inputTextPhoneNumber.error=""
        }
        binding.editTextPasswordSignUp.doAfterTextChanged {
            binding.inputTextPasswordSignUp.error=""
        }
        binding.editTextPasswordConfirm.doAfterTextChanged {
            binding.inputTextPasswordConfirm.error=""
        }
    }
    private fun observers() {
        signupViewModel.signUpValidate.observe(viewLifecycleOwner) {
            if (it.equals(VALIDATE_EMAIL_NULL)) {
                binding.inputTextEmailSignUp.error = getString(R.string.required)
                binding.progressBar.visibility= View.GONE
                binding.btnSignup.setText(R.string.sign_up)
            } else if (it.equals(VALIDATE_FULL_NAME_NULL)) {
                binding.inputTextFullName.error = getString(R.string.required)
                binding.progressBar.visibility= View.GONE
                binding.btnSignup.setText(R.string.sign_up)
            } else if (it.equals(VALIDATE_PHONE_NULL)) {
                binding.inputTextPhoneNumber.error = getString(R.string.required)
                binding.progressBar.visibility= View.GONE
                binding.btnSignup.setText(R.string.sign_up)
            } else if (it.equals(VALIDATE_PASSWORD_NULL)) {
                binding.inputTextPasswordSignUp.error = getString(R.string.required)
                binding.progressBar.visibility= View.GONE
                binding.btnSignup.setText(R.string.sign_up)
            } else if (it.equals(VALIDATE_PASSWORD_CONFIGURATION_NULL)) {
                binding.inputTextPasswordConfirm.error = getString(R.string.required)
                binding.progressBar.visibility= View.GONE
                binding.btnSignup.setText(R.string.sign_up)
            } else if (it.equals(VALIDATE_PASSWORD_DOESNT_MATCH_PROBLEM)) {
                binding.inputTextPasswordConfirm.error = getString(R.string.passwords_doesn_t_match)
                binding.progressBar.visibility= View.GONE
                binding.btnSignup.setText(R.string.sign_up)
            } else if (it.equals(VALIDATE_EMAIL_INVALID)) {
                Toast.makeText(context, VALIDATE_EMAIL_INVALID, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility= View.GONE
                binding.btnSignup.setText(R.string.sign_up)
            } else if (it.equals(VALIDATE_PHONE_INVALID)) {
                Toast.makeText(context, VALIDATE_PHONE_INVALID, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility= View.GONE
                binding.btnSignup.setText(R.string.sign_up)
            } else if (it.equals(VALIDATE_FULL_NAME_INVALID)) {
                Toast.makeText(context, VALIDATE_FULL_NAME_INVALID, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility= View.GONE
                binding.btnSignup.setText(R.string.sign_up)
            } else if (it.equals(VALIDATE_PASSWORD_INVALID)) {
                Toast.makeText(context, VALIDATE_PASSWORD_INVALID, Toast.LENGTH_LONG).show()
                binding.progressBar.visibility= View.GONE
                binding.btnSignup.setText(R.string.sign_up)
            }else if (it.equals(TRUE)) {
                Toast.makeText(context, EMAIL_REGISTERED, Toast.LENGTH_LONG).show()
                binding.progressBar.visibility= View.GONE
                binding.btnSignup.setText(R.string.sign_up)
            } else if (it.equals(FALSE)) {
                val email = binding.editTextEmailSingUp.text.toString().trim()
                val fullName = binding.editTextFullName.text.toString().trim()
                val phone = binding.editTextPhoneNumber.text.toString().trim()
                val password = binding.editTextPasswordSignUp.text.toString()
                signupViewModel.createAccount(UserSignupModel(email,fullName,password,phone))
                binding.progressBar.visibility= View.GONE
                binding.btnSignup.setText(R.string.sign_up)
            } else{
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility= View.GONE
                binding.btnSignup.setText(R.string.sign_up)
            }
        }
        signupViewModel.responseLiveData.observe(viewLifecycleOwner) {
                findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToProfileImageFragment())
                binding.progressBar.visibility= View.GONE
                binding.btnSignup.setText(R.string.sign_up)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}