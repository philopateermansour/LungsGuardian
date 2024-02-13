package com.example.lungsguardian.ui.auth.reset

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lungsguardian.VALIDATE_CODE_INVALID
import com.example.lungsguardian.VALIDATE_CODE_NULL
import com.example.lungsguardian.VALIDATE_EMAIL_INVALID
import com.example.lungsguardian.VALIDATE_EMAIL_NULL
import com.example.lungsguardian.VALIDATE_PASSWORD_CONFIGURATION_NULL
import com.example.lungsguardian.VALIDATE_PASSWORD_DOESNT_MATCH_PROBLEM
import com.example.lungsguardian.VALIDATE_PASSWORD_INVALID
import com.example.lungsguardian.VALIDATE_PASSWORD_NULL
import com.example.lungsguardian.data.model.ResetPasswordModel
import com.example.lungsguardian.data.model.SignupResponse
import com.example.lungsguardian.data.repository.IRepo
import com.example.lungsguardian.data.repository.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ResetViewModel @Inject constructor(private val repo: IRepo) :ViewModel() {

    private val _resetValidate =MutableLiveData<String>()
    val resetValidate get() = _resetValidate

    private val _responseLiveData = MutableLiveData<Response<String>>()
    val responseLiveData get() = _responseLiveData


    private val codePattern = Regex("\\d{6}")
    private val passwordPattern = Regex(
        "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#\$%^&*()-_+=<>?{}|./,:;]).{8,}$"
    )
    fun validate(email:String,code:String,password:String,confirmPassword:String){
        if (email.isEmpty()){
            resetValidate.value = VALIDATE_EMAIL_NULL
        } else if (!isEmailValid(email)){
            resetValidate.value = VALIDATE_EMAIL_INVALID
        } else if(code.toString().isEmpty()){
            resetValidate.value = VALIDATE_CODE_NULL
        } else if (!isCodeValid(code.toString())){
            resetValidate.value = VALIDATE_CODE_INVALID
        } else if(password.isEmpty()){
            resetValidate.value = VALIDATE_PASSWORD_NULL
        } else if (!isPasswordValid(password)){
            resetValidate.value = VALIDATE_PASSWORD_INVALID
        }else if(confirmPassword.isEmpty()){
            resetValidate.value = VALIDATE_PASSWORD_CONFIGURATION_NULL
        } else if (password != confirmPassword){
            resetValidate.value = VALIDATE_PASSWORD_DOESNT_MATCH_PROBLEM
        }else{
            resetPassword(ResetPasswordModel(code,confirmPassword,email,password))
        }
    }

    private fun resetPassword(resetPasswordModel: ResetPasswordModel){
        viewModelScope.launch {
            repo.resetPassword(resetPasswordModel) {
                responseLiveData.value = it
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private fun isPasswordValid(password: String): Boolean {
        return password.matches(passwordPattern)
    }
    private fun isCodeValid(code : String): Boolean {
        return code.matches(codePattern)
    }

}