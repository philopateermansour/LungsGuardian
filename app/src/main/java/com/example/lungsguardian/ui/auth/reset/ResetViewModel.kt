package com.example.lungsguardian.ui.auth.reset

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lungsguardian.utils.VALIDATE_CODE_INVALID
import com.example.lungsguardian.utils.VALIDATE_CODE_NULL
import com.example.lungsguardian.utils.VALIDATE_EMAIL_INVALID
import com.example.lungsguardian.utils.VALIDATE_EMAIL_NULL
import com.example.lungsguardian.utils.VALIDATE_PASSWORD_CONFIGURATION_NULL
import com.example.lungsguardian.utils.VALIDATE_PASSWORD_DOESNT_MATCH_PROBLEM
import com.example.lungsguardian.utils.VALIDATE_PASSWORD_INVALID
import com.example.lungsguardian.utils.VALIDATE_PASSWORD_NULL
import com.example.lungsguardian.data.model.ResetPasswordModel
import com.example.lungsguardian.data.repository.IRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
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
         if(code.toString().isEmpty()){
            _resetValidate.value = VALIDATE_CODE_NULL
        } else if (!isCodeValid(code.toString())){
            _resetValidate.value = VALIDATE_CODE_INVALID
        } else if(password.isEmpty()){
            _resetValidate.value = VALIDATE_PASSWORD_NULL
        } else if (!isPasswordValid(password)){
            _resetValidate.value = VALIDATE_PASSWORD_INVALID
        }else if(confirmPassword.isEmpty()){
            _resetValidate.value = VALIDATE_PASSWORD_CONFIGURATION_NULL
        } else if (password != confirmPassword){
            _resetValidate.value = VALIDATE_PASSWORD_DOESNT_MATCH_PROBLEM
        }else{
            resetPassword(ResetPasswordModel(code,confirmPassword,email,password))
        }
    }

    private fun resetPassword(resetPasswordModel: ResetPasswordModel){
        viewModelScope.launch(Dispatchers.IO)
        {try{
            repo.resetPassword(resetPasswordModel) {
                if (it?.code()==200){
                    _responseLiveData.postValue(it)
                }else{
                    _resetValidate.postValue(it?.message())
                }
            }
        }catch (e:IOException){
            e.printStackTrace()
            _resetValidate.postValue(e.localizedMessage)
        }
        }
    }


    private fun isPasswordValid(password: String): Boolean {
        return password.matches(passwordPattern)
    }
    private fun isCodeValid(code : String): Boolean {
        return code.matches(codePattern)
    }

}