package com.example.lungsguardian.ui.home.profile.edit.email

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.lungsguardian.R
import com.example.lungsguardian.databinding.BottomSheetFragmentEditEmailBinding
import com.example.lungsguardian.ui.home.activity.HomeSharedViewModel
import com.example.lungsguardian.utils.EMAIL_REGISTERED
import com.example.lungsguardian.utils.VALIDATE_EMAIL_INVALID
import com.example.lungsguardian.utils.VALIDATE_EMAIL_NULL
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditEmailBottomSheetFragment :BottomSheetDialogFragment() {
    private var _binding :BottomSheetFragmentEditEmailBinding ?=null
    private val binding get() = _binding!!
    private val editEmailViewModel : EditEmailViewModel by viewModels()
    private val homeSharedViewModel: HomeSharedViewModel by activityViewModels()


    companion object{
        fun getIncs()= EditEmailBottomSheetFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_fragment_edit_email, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = BottomSheetFragmentEditEmailBinding.bind(view)
        onClicks()
        observers()
    }

    private fun onClicks() {
        binding.btnResetEmail.setOnClickListener {
            val email = binding.editTextResetEmail.text.toString().trim()
            binding.progressBar.visibility= View.VISIBLE
            binding.btnResetEmail.text=null
            lifecycleScope.launch {
            editEmailViewModel.validation(email) }
        }
        binding.editTextResetEmail.doAfterTextChanged {
            binding.inputTextResetEmail.error=""
        }
    }

    private fun observers() {
        editEmailViewModel.editEmailValidateLiveData.observe(viewLifecycleOwner){
            if (it.equals(VALIDATE_EMAIL_NULL)){
                binding.inputTextResetEmail.error=getString(R.string.required)
                binding.progressBar.visibility= View.GONE
                binding.btnResetEmail.setText(R.string.reset_email)
            } else if(it.equals(VALIDATE_EMAIL_INVALID)){
                binding.inputTextResetEmail.error= VALIDATE_EMAIL_INVALID
                binding.progressBar.visibility= View.GONE
                binding.btnResetEmail.setText(R.string.reset_email)
            } else if(it.equals(EMAIL_REGISTERED)){
                Toast.makeText(context, EMAIL_REGISTERED, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility= View.GONE
                binding.btnResetEmail.setText(R.string.reset_email)
            } else {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility= View.GONE
                binding.btnResetEmail.setText(R.string.reset_email)
            }
        }
        editEmailViewModel.editEmailResponseLiveData.observe(viewLifecycleOwner){
            Toast.makeText(context, it.body(), Toast.LENGTH_SHORT).show()
            binding.progressBar.visibility= View.GONE
            binding.btnResetEmail.setText(R.string.reset_email)
            dismiss()
            homeSharedViewModel.isProfileChanged.value=true
        }
    }
}