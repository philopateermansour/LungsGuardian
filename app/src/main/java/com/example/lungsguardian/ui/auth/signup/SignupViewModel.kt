package com.example.lungsguardian.ui.auth.signup

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lungsguardian.data.model.UserResponseModel
import com.example.lungsguardian.data.model.UserSignupModel
import com.example.lungsguardian.data.repository.IRepo
import com.example.lungsguardian.utils.EMAIL_REGISTERED
import com.example.lungsguardian.utils.LOGGED_IN
import com.example.lungsguardian.utils.LOGGED_STATE
import com.example.lungsguardian.utils.MySharedPreferences
import com.example.lungsguardian.utils.TRUE
import com.example.lungsguardian.utils.USER_TOKEN
import com.example.lungsguardian.utils.VALIDATE_EMAIL_INVALID
import com.example.lungsguardian.utils.VALIDATE_EMAIL_NULL
import com.example.lungsguardian.utils.VALIDATE_FULL_NAME_INVALID
import com.example.lungsguardian.utils.VALIDATE_FULL_NAME_NULL
import com.example.lungsguardian.utils.VALIDATE_PASSWORD_CONFIGURATION_NULL
import com.example.lungsguardian.utils.VALIDATE_PASSWORD_DOESNT_MATCH_PROBLEM
import com.example.lungsguardian.utils.VALIDATE_PASSWORD_INVALID
import com.example.lungsguardian.utils.VALIDATE_PASSWORD_NULL
import com.example.lungsguardian.utils.VALIDATE_PHONE_INVALID
import com.example.lungsguardian.utils.VALIDATE_PHONE_NULL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class SignupViewModel @Inject constructor(private val repo: IRepo) : ViewModel() {
    private val _signUpValidate = MutableLiveData<String>()
    val signUpValidate get() = _signUpValidate
    private val _responseLiveData = MutableLiveData<Response<UserResponseModel>>()
    val responseLiveData get() = _responseLiveData
    private val phoneNumberPattern = Regex("\\d{11}")
    private val passwordPattern = Regex(
        "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#\$%^&*()-_+=<>?{}|./,:;]).{8,}$"
    )

    suspend fun validate(
        email: String,
        fullName: String,
        phone: String,
        password: String,
        confirmPassword: String
    ) {

        if (email.isEmpty()) {
            _signUpValidate.value = VALIDATE_EMAIL_NULL
        } else if (!isEmailValid(email)) {
            _signUpValidate.value = VALIDATE_EMAIL_INVALID
        } else if (fullName.isEmpty()) {
            _signUpValidate.value = VALIDATE_FULL_NAME_NULL
        } else if (!isFullNameValid(fullName)) {
            _signUpValidate.value = VALIDATE_FULL_NAME_INVALID
        } else if (phone.isEmpty()) {
            _signUpValidate.value = VALIDATE_PHONE_NULL
        } else if (!isPhoneValid(phone)) {
            _signUpValidate.value = VALIDATE_PHONE_INVALID
        } else if (password.isEmpty()) {
            _signUpValidate.value = VALIDATE_PASSWORD_NULL
        } else if (!isPasswordValid(password)) {
            _signUpValidate.value = VALIDATE_PASSWORD_INVALID
        } else if (confirmPassword.isEmpty()) {
            _signUpValidate.value = VALIDATE_PASSWORD_CONFIGURATION_NULL
        } else if (password != confirmPassword) {
            _signUpValidate.value = VALIDATE_PASSWORD_DOESNT_MATCH_PROBLEM
        } else if (checkIfEmailExists(email).equals(TRUE)) {
            _signUpValidate.value = EMAIL_REGISTERED
        } else {
            createAccount(UserSignupModel(email, fullName, password, phone))
        }
    }


    private fun createAccount(user: UserSignupModel) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.createAccount(user) {
                    if (it?.code() == 200) {
                        _responseLiveData.postValue(it)
                        cacheUserDate(it)
                    } else {
                        _signUpValidate.postValue(it?.message())
                    }
                }
            } catch (e: IOException) {
                _signUpValidate.postValue(e.localizedMessage)
            }
        }
    }

    private fun cacheUserDate(it: Response<UserResponseModel>?) {
        MySharedPreferences.setInShared(USER_TOKEN, it!!.body()!!.token)
        MySharedPreferences.setInShared(LOGGED_STATE, LOGGED_IN)
    }

    /*private suspend fun checkIfEmailExists(email: String) :String {
        var response = ""
       val job = viewModelScope.launch(Dispatchers.IO){
              repo.checkIfEmailExists(email)
        }
        job.join()
        return response
    }*/
    private suspend fun checkIfEmailExists(email: String) :String {
        var response = ""
       val job =  viewModelScope.launch(Dispatchers.IO){
              repo.checkIfEmailExists(email){
                response=it!!
            } }
        job.join()
        return response
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