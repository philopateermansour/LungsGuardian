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
import com.example.lungsguardian.utils.VALIDATE_CODE_INVALID
import com.example.lungsguardian.utils.VALIDATE_CODE_NULL
import com.example.lungsguardian.utils.VALIDATE_EMAIL_INVALID
import com.example.lungsguardian.utils.VALIDATE_EMAIL_NULL
import com.example.lungsguardian.utils.VALIDATE_PASSWORD_CONFIGURATION_NULL
import com.example.lungsguardian.utils.VALIDATE_PASSWORD_DOESNT_MATCH_PROBLEM
import com.example.lungsguardian.utils.VALIDATE_PASSWORD_INVALID
import com.example.lungsguardian.utils.VALIDATE_PASSWORD_NULL
import com.example.lungsguardian.databinding.FragmentResetBinding
import com.example.lungsguardian.ui.auth.forget.ForgetFragment
import com.example.lungsguardian.ui.auth.forget.ForgetFragmentDirections
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
        val receivedEmail = ResetFragmentArgs.fromBundle(requireArguments()).email
        onClicks(receivedEmail)
        observers()
    }

    private fun onClicks(receivedEmail :String) {
        binding.btnBackToSendCode.setOnClickListener{
            findNavController().navigate(ResetFragmentDirections.actionResetFragmentToForgetFragment())
        }
        binding.btnReset.setOnClickListener{
            val code = binding.editTextCode.text.toString().trim()
            val password= binding.editTextPasswordReset.text.toString()
            val confirmPassword = binding.editTextPasswordConfirm.text.toString()
            binding.progressBar.visibility= View.VISIBLE
            binding.btnReset.text=null
            resetViewModel.validate(receivedEmail,code,password,confirmPassword)
        }
        binding.editTextCode.doAfterTextChanged {
            binding.inputTextCode.error=""
        }
        binding.editTextPasswordReset.doAfterTextChanged {
            binding.inputTextPasswordReset.error=""
        }
        binding.editTextPasswordConfirm.doAfterTextChanged {
            binding.inputTextPasswordConfirm.error=""
        }
    }

    private fun observers() {
        resetViewModel.resetValidate.observe(viewLifecycleOwner) {
            if (it.equals(VALIDATE_CODE_NULL)) {
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
            } else if (it.equals(VALIDATE_CODE_INVALID)) {
                Toast.makeText(context, VALIDATE_CODE_INVALID, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                binding.btnReset.setText(R.string.reset)
            } else if (it.equals(VALIDATE_PASSWORD_INVALID)) {
                Toast.makeText(context, VALIDATE_PASSWORD_INVALID, Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.GONE
                binding.btnReset.setText(R.string.reset)
            } else{
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility= View.GONE
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
                Toast.makeText(context,it.message(),Toast.LENGTH_SHORT).show()
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