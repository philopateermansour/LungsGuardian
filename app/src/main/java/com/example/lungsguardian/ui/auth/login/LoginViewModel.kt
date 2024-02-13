package com.example.lungsguardian.ui.auth.login

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lungsguardian.VALIDATE_EMAIL_INVALID
import com.example.lungsguardian.VALIDATE_EMAIL_NULL
import com.example.lungsguardian.VALIDATE_PASSWORD_NULL
import com.example.lungsguardian.data.model.LoginResponse
import com.example.lungsguardian.data.model.UserLoginModel
import com.example.lungsguardian.data.repository.IRepo
import com.example.lungsguardian.data.repository.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repo: IRepo) : ViewModel() {
    private val _loginValidate = MutableLiveData<String>()
    val loginValidate get() = _loginValidate
    private val _responseLiveData = MutableLiveData<Response<LoginResponse>>()
    val responseLiveData get() = _responseLiveData

    fun validate(email: String, password: String) {
        if (email.isEmpty()) {
            loginValidate.value = VALIDATE_EMAIL_NULL
        } else if (!isEmailValid(email)) {
            loginValidate.value = VALIDATE_EMAIL_INVALID
        } else if (password.isEmpty()) {
            loginValidate.value = VALIDATE_PASSWORD_NULL
        } else {
            login(UserLoginModel(email, password))
        }
    }

    fun login(user: UserLoginModel) {
        viewModelScope.launch {
            repo.login(user) {
                responseLiveData.value = it
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


}