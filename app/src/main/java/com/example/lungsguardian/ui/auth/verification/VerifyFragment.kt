package com.example.lungsguardian.ui.auth.verification

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.lungsguardian.R
import com.example.lungsguardian.databinding.FragmentVerifyBinding
import com.example.lungsguardian.ui.home.activity.HomeActivity
import com.example.lungsguardian.utils.VALIDATE_CODE_NULL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyFragment : Fragment() {

    private var _binding:FragmentVerifyBinding ?=null
    private val binding get() = _binding!!
    private val verifyViewModel :VerifyViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verify, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding= FragmentVerifyBinding.bind(view)
        onClicks()
        observers()
    }

    private fun onClicks() {
        binding.btnSendCode.setOnClickListener {
            val code=binding.editTextConfirmCode.text.toString()
            binding.progressBar.visibility= View.VISIBLE
            binding.btnSendCode.text=null
            verifyViewModel.validate(VerifyFragmentArgs.fromBundle(requireArguments()).email,code)
        }
        binding.editTextConfirmCode.doAfterTextChanged {
            binding.inputTextConfirmCode.error=""
        }
    }
    private fun observers() {
        verifyViewModel.errorLiveData.observe(viewLifecycleOwner){
           if (it.equals(VALIDATE_CODE_NULL)){
               binding.inputTextConfirmCode.error=getString(R.string.required)
               binding.progressBar.visibility= View.GONE
               binding.btnSendCode.setText(R.string.send_code)
           }
           else{
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility= View.GONE
                binding.btnSendCode.setText(R.string.send_code)
        }
        }
        verifyViewModel.confirmEmailLiveData.observe(viewLifecycleOwner){
            val intent = Intent(activity, HomeActivity::class.java)
            startActivity(intent)
            activity?.finish()
            binding.progressBar.visibility= View.GONE
            binding.btnSendCode.setText(R.string.send_code)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}