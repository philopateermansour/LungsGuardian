package com.example.lungsguardian.ui.auth.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lungsguardian.VALIDATE_EMAIL_PROBLEM
import com.example.lungsguardian.VALIDATE_PASSWORD_PROBLEM
import com.example.lungsguardian.data.model.LoginResponse
import com.example.lungsguardian.data.model.UserLoginModel
import com.example.lungsguardian.data.repository.Repo
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private val _loginValidate = MutableLiveData<String>()
    val loginValidate get() = _loginValidate
    val responseLiveData = MutableLiveData<Response<LoginResponse>>()
    private var repo: Repo = Repo()

    fun validate(email: String, password: String) {
        if (email.isEmpty()) {
            loginValidate.value = VALIDATE_EMAIL_PROBLEM
        } else if (password.isEmpty()) {
            loginValidate.value = VALIDATE_PASSWORD_PROBLEM
        }
        else{
            login(UserLoginModel(email,password))
        }
    }
    fun login(user:UserLoginModel){
        repo.login(user){
            responseLiveData.value = it
        }
    }
}