package com.example.lungsguardian.ui.auth.login

import android.content.Intent
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
import com.example.lungsguardian.utils.VALIDATE_EMAIL_INVALID
import com.example.lungsguardian.utils.VALIDATE_EMAIL_NULL
import com.example.lungsguardian.utils.VALIDATE_PASSWORD_NULL
import com.example.lungsguardian.databinding.FragmentLoginBinding
import com.example.lungsguardian.ui.home.activity.HomeActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?

    ): View {
        // Inflate the layout for this fragment
        _binding=FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)
        onClicks()
        observers()
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

            binding.progressBar.visibility= View.VISIBLE
            binding.btnLogin.text=null
            loginViewModel.validate(email, password)
        }
        binding.editTextEmailLogin.doAfterTextChanged {
            binding.inputTextEmailLogin.error=""
        }
        binding.editTextPasswordLogin.doAfterTextChanged {
            binding.inputTextPasswordLogin.error=""
        }
    }

    private fun observers() {
        loginViewModel.loginValidate.observe(viewLifecycleOwner) {
            if (it.equals(VALIDATE_EMAIL_NULL)) {
                binding.inputTextEmailLogin.error = getString(R.string.required)
                binding.progressBar.visibility= View.GONE
                binding.btnLogin.setText(R.string.login)
            } else if (it.equals(VALIDATE_PASSWORD_NULL)) {
                binding.inputTextPasswordLogin.error = getString(R.string.required)
                binding.progressBar.visibility= View.GONE
                binding.btnLogin.setText(R.string.login)
            } else if (it.equals(VALIDATE_EMAIL_INVALID)) {
                Toast.makeText(context, VALIDATE_EMAIL_INVALID, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility= View.GONE
                binding.btnLogin.setText(R.string.login)
            } else{
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility= View.GONE
                binding.btnLogin.setText(R.string.login)
            }
        }
        loginViewModel.responseLiveData.observe(viewLifecycleOwner) {
                val intent = Intent(activity, HomeActivity::class.java)
                startActivity(intent)
                activity?.finish()
                binding.progressBar.visibility= View.GONE
                binding.btnLogin.setText(R.string.login)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}