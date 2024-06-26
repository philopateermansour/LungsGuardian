package com.example.lungsguardian.ui.home.profile.edit.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.lungsguardian.R
import com.example.lungsguardian.databinding.BottomSheetFragmentChangePasswordBinding
import com.example.lungsguardian.ui.home.activity.HomeSharedViewModel
import com.example.lungsguardian.utils.VALIDATE_PASSWORD_CONFIGURATION_NULL
import com.example.lungsguardian.utils.VALIDATE_PASSWORD_INVALID
import com.example.lungsguardian.utils.VALIDATE_PASSWORD_NULL
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordBottomSheetFragment :BottomSheetDialogFragment() {

    private var _binding :BottomSheetFragmentChangePasswordBinding ?=null
    private val binding get() = _binding!!
    private val changePasswordViewModel :ChangePasswordViewModel by viewModels()
    private val homeSharedViewModel: HomeSharedViewModel by activityViewModels()


    companion object{
        fun getIncs()= ChangePasswordBottomSheetFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_fragment_change_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding= BottomSheetFragmentChangePasswordBinding.bind(view)
        onClicks()
        observers()
    }

    private fun onClicks() {
        binding.btnChangePassword.setOnClickListener {
            val oldPassword = binding.editTextOldPassword.text.toString()
            val newPassword = binding.editTextNewPassword.text.toString()
            binding.progressBar.visibility= View.VISIBLE
            binding.btnChangePassword.text=null
            changePasswordViewModel.validate(oldPassword,newPassword)
        }
        binding.editTextOldPassword.doAfterTextChanged {
            binding.inputTextOldPassword.error=""
        }
        binding.editTextNewPassword.doAfterTextChanged {
            binding.inputTextNewPassword.error=""
        }
    }
    private fun observers() {
        changePasswordViewModel.signUpValidate.observe(viewLifecycleOwner){
            if (it.equals(VALIDATE_PASSWORD_NULL)){
                binding.inputTextOldPassword.error=getString(R.string.required)
                binding.progressBar.visibility= View.GONE
                binding.btnChangePassword.setText(R.string.change_password)
            } else if(it.equals(VALIDATE_PASSWORD_CONFIGURATION_NULL)){
                binding.inputTextNewPassword.error=getString(R.string.required)
                binding.progressBar.visibility= View.GONE
                binding.btnChangePassword.setText(R.string.change_password)
            } else if(it.equals(VALIDATE_PASSWORD_INVALID)){
                binding.inputTextNewPassword.error= VALIDATE_PASSWORD_INVALID
                binding.progressBar.visibility= View.GONE
                binding.btnChangePassword.setText(R.string.change_password)
            } else{
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility= View.GONE
                binding.btnChangePassword.setText(R.string.change_password)
            }
        }
        changePasswordViewModel.responseLiveData.observe(viewLifecycleOwner){
            Toast.makeText(context, it.body(), Toast.LENGTH_SHORT).show()
            binding.progressBar.visibility= View.GONE
            binding.btnChangePassword.setText(R.string.change_password)
            dismiss()
            homeSharedViewModel.isProfileChanged.value=true
        }
    }
}