package com.example.lungsguardian.ui.auth.reset

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
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
import com.example.lungsguardian.ui.home.activity.HomeActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ResetFragment : Fragment() {

    private var _binding: FragmentResetBinding? = null
    val binding get() = _binding!!

    private val resetViewModel : ResetViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentResetBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            binding.btnReset.text=null
            resetViewModel.validate(email,code,password,confirmPassword)
        }
        binding.editTextEmailReset.doAfterTextChanged {
            binding.inputTextEmailReset.isErrorEnabled=false
        }
        binding.editTextCode.doAfterTextChanged {
            binding.inputTextCode.isErrorEnabled=false
        }
        binding.editTextPasswordReset.doAfterTextChanged {
            binding.inputTextPasswordReset.isErrorEnabled=false
        }
        binding.editTextPasswordConfirm.doAfterTextChanged {
            binding.inputTextPasswordConfirm.isErrorEnabled=false
        }
    }

    private fun observe() {
        resetViewModel.resetValidate.observe(viewLifecycleOwner) {
            if (it.equals(VALIDATE_EMAIL_NULL)) {
                binding.inputTextEmailReset.error = getString(R.string.required)
                binding.progressBar.visibility = View.GONE
                binding.btnReset.setText(R.string.reset)
            } else if (it.equals(VALIDATE_CODE_NULL)) {
                binding.inputTextCode.error = getString(R.string.required)
                binding.progressBar.visibility = View.GONE
                binding.btnReset.setText(R.string.reset)
            } else if (it.equals(VALIDATE_PASSWORD_NULL)) {
                binding.inputTextPasswordReset.error = getString(R.string.required)
                binding.progressBar.visibility = View.GONE
                binding.btnReset.setText(R.string.reset)
            } else if (it.equals(VALIDATE_PASSWORD_CONFIGURATION_NULL)) {
                binding.inputTextPasswordConfirm.error = getString(R.string.required)
                binding.progressBar.visibility = View.GONE
                binding.btnReset.setText(R.string.reset)
            } else if (it.equals(VALIDATE_PASSWORD_DOESNT_MATCH_PROBLEM)) {
                binding.inputTextPasswordConfirm.error = getString(R.string.passwords_doesn_t_match)
                binding.progressBar.visibility = View.GONE
                binding.btnReset.setText(R.string.reset)
            } else if (it.equals(VALIDATE_EMAIL_INVALID)) {
                Toast.makeText(context, VALIDATE_EMAIL_INVALID, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                binding.btnReset.setText(R.string.reset)
            } else if (it.equals(VALIDATE_CODE_INVALID)) {
                Toast.makeText(context, VALIDATE_CODE_INVALID, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                binding.btnReset.setText(R.string.reset)
            } else if (it.equals(VALIDATE_PASSWORD_INVALID)) {
                Toast.makeText(context, VALIDATE_PASSWORD_INVALID, Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.GONE
                binding.btnReset.setText(R.string.reset)
            }
        }
        resetViewModel.responseLiveData.observe(viewLifecycleOwner){
            if (it.code()==200){
                val intent = Intent(activity, HomeActivity::class.java)
                startActivity(intent)
                activity?.finish()
                binding.progressBar.visibility= View.GONE
                binding.btnReset.setText(R.string.reset)
            }else{
                Toast.makeText(context,"Invalid code or email",Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility= View.GONE
                binding.btnReset.setText(R.string.reset)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}