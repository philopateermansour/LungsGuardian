package com.example.lungsguardian.ui.auth.login

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
import com.example.lungsguardian.VALIDATE_PASSWORD_NULL
import com.example.lungsguardian.databinding.FragmentLoginBinding
import com.example.lungsguardian.ui.home.HomeActivity


class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)

        onClicks()
        observe()


    }


    private fun onClicks() {
        binding.register.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignupFragment())
        }
        binding.textForgot.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToForgetFragment())
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.editTextEmailLogin.text.toString().trim()
            val password = binding.editTextPasswordLogin.text.toString()
            loginViewModel.validate(email, password)
        }
    }

    private fun observe() {
        loginViewModel.loginValidate.observe(viewLifecycleOwner) {
            if (it.equals(VALIDATE_EMAIL_NULL)) {
                binding.inputTextEmailLogin.error = getString(R.string.required)
            } else if (it.equals(VALIDATE_PASSWORD_NULL)) {
                binding.inputTextPasswordLogin.error = getString(R.string.required)
            } else if (it.equals(VALIDATE_EMAIL_INVALID)) {
                Toast.makeText(context, VALIDATE_EMAIL_INVALID, Toast.LENGTH_SHORT).show()
                binding.inputTextEmailLogin.isErrorEnabled = false
            }

        }
        loginViewModel.responseLiveData.observe(viewLifecycleOwner) {
            if (it.code() == 200) {
                val intent = Intent(activity, HomeActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
            else if (it.code() == 401){
                binding.inputTextPasswordLogin.isErrorEnabled = false
                Toast.makeText(context,"wrong email or password",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}