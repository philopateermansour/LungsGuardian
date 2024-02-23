package com.example.lungsguardian.ui.home.profile.edit.password

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lungsguardian.data.model.UserResponseModel
import com.example.lungsguardian.data.repository.IRepo
import com.example.lungsguardian.utils.VALIDATE_PASSWORD_CONFIGURATION_NULL
import com.example.lungsguardian.utils.VALIDATE_PASSWORD_INVALID
import com.example.lungsguardian.utils.VALIDATE_PASSWORD_NULL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(private val repo :IRepo) :ViewModel() {

    private val _changePasswordValidate = MutableLiveData<String>()
    val signUpValidate get() = _changePasswordValidate
    private val _responseLiveData = MutableLiveData<Response<String>>()
    val responseLiveData get() = _responseLiveData

    private val passwordPattern = Regex(
        "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#\$%^&*()-_+=<>?{}|./,:;]).{8,}$"
    )
    fun validate(oldPassword: String, newPassword: String) {
        if (oldPassword.isEmpty()){
            _changePasswordValidate.value = VALIDATE_PASSWORD_NULL

        } else if (newPassword.isEmpty()){
            _changePasswordValidate.value = VALIDATE_PASSWORD_CONFIGURATION_NULL
        } else if (!isPasswordValid(newPassword)){
            _changePasswordValidate.value = VALIDATE_PASSWORD_INVALID
        } else {
            changePassword(oldPassword,newPassword)
        }
    }

    private fun changePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch(Dispatchers.IO){
            try {
            repo.changePassword(oldPassword,newPassword){
                if (it?.code()==200){
                    _responseLiveData.postValue(it)}
                    else{
                        _changePasswordValidate.postValue(it?.body())
                    }
            }}catch (e:IOException){
                e.printStackTrace()
                _changePasswordValidate.postValue(e.localizedMessage)
            }
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.matches(passwordPattern)
    }
}