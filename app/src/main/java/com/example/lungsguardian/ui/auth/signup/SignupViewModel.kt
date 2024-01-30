package com.example.lungsguardian.ui.auth.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lungsguardian.VALIDATE_EMAIL_PROBLEM
import com.example.lungsguardian.VALIDATE_FULLNAME_PROBLEM
import com.example.lungsguardian.VALIDATE_PASSWORDCONFIGURATION_PROBLEM
import com.example.lungsguardian.VALIDATE_PASSWORDDOESNTMATCH_PROBLEM
import com.example.lungsguardian.VALIDATE_PASSWORD_PROBLEM
import com.example.lungsguardian.VALIDATE_PHONE_PROBLEM
import com.example.lungsguardian.data.model.SignupResponse
import com.example.lungsguardian.data.model.UserSignupModel
import com.example.lungsguardian.data.repository.Repo
import retrofit2.Response

class SignupViewModel : ViewModel() {
    private val _singUpValidate = MutableLiveData<String>()
    val signUpValidate get() = _singUpValidate
    val responseLiveData = MutableLiveData<Response<SignupResponse>>()
    private var repo: Repo = Repo()

    fun validate(
        email: String,
        fullName: String,
        phone: String,
        password: String,
        confirmPassword: String
    ) {

        if (email.isEmpty()) {
            _singUpValidate.value = VALIDATE_EMAIL_PROBLEM
        } else if (fullName.isEmpty()) {
            _singUpValidate.value = VALIDATE_FULLNAME_PROBLEM
        } else if (phone.isEmpty()) {
            _singUpValidate.value = VALIDATE_PHONE_PROBLEM
        } else if (password.isEmpty()) {
            _singUpValidate.value = VALIDATE_PASSWORD_PROBLEM
        } else if (confirmPassword.isEmpty()) {
            _singUpValidate.value = VALIDATE_PASSWORDCONFIGURATION_PROBLEM
        } else if (password != confirmPassword) {
            _singUpValidate.value = VALIDATE_PASSWORDDOESNTMATCH_PROBLEM
        } else {
            createAccount(UserSignupModel(email,fullName,password,phone))
        }

    }


    private fun createAccount(user:UserSignupModel) {
        repo.createAccount(user) {
            responseLiveData.value = it
        }
    }
}