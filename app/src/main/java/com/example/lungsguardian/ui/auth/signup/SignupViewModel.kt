package com.example.lungsguardian.ui.auth.signup

import android.content.Context
import android.content.SharedPreferences
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lungsguardian.VALIDATE_EMAIL_INVALID
import com.example.lungsguardian.VALIDATE_EMAIL_NULL
import com.example.lungsguardian.VALIDATE_FULL_NAME_INVALID
import com.example.lungsguardian.VALIDATE_FULL_NAME_NULL
import com.example.lungsguardian.VALIDATE_PASSWORD_CONFIGURATION_NULL
import com.example.lungsguardian.VALIDATE_PASSWORD_DOESNT_MATCH_PROBLEM
import com.example.lungsguardian.VALIDATE_PASSWORD_INVALID
import com.example.lungsguardian.VALIDATE_PASSWORD_NULL
import com.example.lungsguardian.VALIDATE_PHONE_INVALID
import com.example.lungsguardian.VALIDATE_PHONE_NULL
import com.example.lungsguardian.data.model.SignupResponse
import com.example.lungsguardian.data.model.UserSignupModel
import com.example.lungsguardian.data.repository.IRepo
import com.example.lungsguardian.data.repository.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class SignupViewModel @Inject constructor(private val repo: IRepo) : ViewModel() {
    private val _signUpValidate = MutableLiveData<String>()
    val signUpValidate get() = _signUpValidate
    private val _emailExistsValidate = MutableLiveData<String>()
    val emailExistsValidate get() = _signUpValidate
    private val _responseLiveData = MutableLiveData<Response<SignupResponse>>()
    val responseLiveData get() = _responseLiveData
    private val phoneNumberPattern = Regex("\\d{11}")
    private val passwordPattern = Regex(
        "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#\$%^&*()-_+=<>?{}|./,:;]).{8,}$"
    )

    fun validate(
        email: String,
        fullName: String,
        phone: String,
        password: String,
        confirmPassword: String
    ) {

        if (email.isEmpty()) {
            signUpValidate.value = VALIDATE_EMAIL_NULL
        } else if (!isEmailValid(email)) {
            signUpValidate.value = VALIDATE_EMAIL_INVALID
        } else if (fullName.isEmpty()) {
            signUpValidate.value = VALIDATE_FULL_NAME_NULL
        } else if (!isFullNameValid(fullName)) {
            signUpValidate.value = VALIDATE_FULL_NAME_INVALID
        } else if (phone.isEmpty()) {
            signUpValidate.value = VALIDATE_PHONE_NULL
        } else if (!isPhoneValid(phone)) {
            signUpValidate.value = VALIDATE_PHONE_INVALID
        } else if (password.isEmpty()) {
            signUpValidate.value = VALIDATE_PASSWORD_NULL
        } else if (!isPasswordValid(password)) {
            signUpValidate.value = VALIDATE_PASSWORD_INVALID
        } else if (confirmPassword.isEmpty()) {
            signUpValidate.value = VALIDATE_PASSWORD_CONFIGURATION_NULL
        } else if (password != confirmPassword) {
            signUpValidate.value = VALIDATE_PASSWORD_DOESNT_MATCH_PROBLEM
        } else {
            createAccount(UserSignupModel(email, fullName, password, phone))
        }}


    private fun createAccount(user: UserSignupModel) {
        viewModelScope.launch {
            repo.createAccount(user) {
                responseLiveData.value = it
            }
        }
    }
    private fun checkIfEmailExists(email: String) {
        repo.checkIfEmailExists(email){
            emailExistsValidate.value = it?.body()
        }
    }
    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private fun isFullNameValid(fullName: String): Boolean {
        return fullName.contains(" ")
    }

    private fun isPhoneValid(phone: String): Boolean {
        return phone.matches(phoneNumberPattern)
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.matches(passwordPattern)
    }
}