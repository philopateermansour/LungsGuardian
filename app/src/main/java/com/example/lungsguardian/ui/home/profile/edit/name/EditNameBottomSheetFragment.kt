package com.example.lungsguardian.ui.home.profile.edit.name

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.example.lungsguardian.R
import com.example.lungsguardian.databinding.BottomSheetFragmentEditNameBinding
import com.example.lungsguardian.utils.TRUE
import com.example.lungsguardian.utils.VALIDATE_FULL_NAME_INVALID
import com.example.lungsguardian.utils.VALIDATE_FULL_NAME_NULL
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EditNameBottomSheetFragment :BottomSheetDialogFragment(){

    private var _binding :BottomSheetFragmentEditNameBinding ?=null
    private val binding get() = _binding!!
    private val editNameViwModel :EditNameViewModel by viewModels()


    companion object{
        fun getIncs()=EditNameBottomSheetFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_fragment_edit_name,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding= BottomSheetFragmentEditNameBinding.bind(view)
        onClicks()
        observers()
    }

    private fun onClicks() {
        binding.btnResetName.setOnClickListener {
            val name =binding.editTextFullName.text.toString().trim()
            binding.progressBar.visibility= View.VISIBLE
            binding.btnResetName.text=null
            editNameViwModel.validate(name)
        }
        binding.editTextFullName.doAfterTextChanged {
            binding.inputTextFullName.error=""
        }
    }
    private  fun observers() {
        editNameViwModel.validateLiveData.observe(viewLifecycleOwner){
            if (it.equals(VALIDATE_FULL_NAME_NULL)){
                binding.inputTextFullName.error=getString(R.string.required)
                binding.progressBar.visibility= View.GONE
                binding.btnResetName.setText(R.string.reset_name)
            } else if (it.equals(VALIDATE_FULL_NAME_INVALID)){
                binding.inputTextFullName.error= VALIDATE_FULL_NAME_INVALID
                binding.progressBar.visibility= View.GONE
                binding.btnResetName.setText(R.string.reset_name)
            } else{
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility= View.GONE
                binding.btnResetName.setText(R.string.reset_name)
            }
        }
        editNameViwModel.editNameResponseLiveData.observe(viewLifecycleOwner){
            Toast.makeText(context, it.body(), Toast.LENGTH_SHORT).show()
            binding.progressBar.visibility= View.GONE
            binding.btnResetName.setText(R.string.reset_name)
            dismiss()
        }
    }

}