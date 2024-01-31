package com.example.lungsguardian.ui.auth.login

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lungsguardian.VALIDATE_EMAIL_INVALID
import com.example.lungsguardian.VALIDATE_EMAIL_NULL
import com.example.lungsguardian.VALIDATE_PASSWORD_NULL
import com.example.lungsguardian.data.model.LoginResponse
import com.example.lungsguardian.data.model.UserLoginModel
import com.example.lungsguardian.data.repository.Repo
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private val _loginValidate = MutableLiveData<String>()
    val loginValidate get() = _loginValidate
    private val _responseLiveData = MutableLiveData<Response<LoginResponse>>()
    val responseLiveData get() = _responseLiveData
    private var repo: Repo = Repo()

    fun validate(email: String, password: String) {
        if (email.isEmpty()) {
            loginValidate.value = VALIDATE_EMAIL_NULL
        } else if (!isEmailValid(email)){
            loginValidate.value = VALIDATE_EMAIL_INVALID
        } else if (password.isEmpty()) {
            loginValidate.value = VALIDATE_PASSWORD_NULL
        } else {
            login(UserLoginModel(email, password))
        }
    }

    fun login(user: UserLoginModel) {
        repo.login(user) {
            responseLiveData.value = it
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


}