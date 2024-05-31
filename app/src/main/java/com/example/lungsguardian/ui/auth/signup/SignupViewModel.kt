package com.example.lungsguardian.ui.auth.signup

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lungsguardian.data.model.UserResponseModel
import com.example.lungsguardian.data.model.UserSignupModel
import com.example.lungsguardian.data.repository.IRepo
import com.example.lungsguardian.utils.FALSE
import com.example.lungsguardian.utils.LOGGED_IN
import com.example.lungsguardian.utils.LOGGED_STATE
import com.example.lungsguardian.utils.MySharedPreferences
import com.example.lungsguardian.utils.TRUE
import com.example.lungsguardian.utils.USER_NAME
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
    private val _responseCode = MutableLiveData<Response<String>>()
    val responseCode get() = _responseCode
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
        } else {
            checkIfEmailExists(UserSignupModel(email, fullName, password, phone))
        }
    }
     fun createAccount(user: UserSignupModel) {
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
    fun sendCode(email: String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                repo.sendCodeToConfirm(email){
                    if (it?.code() == 200) {
                        _responseCode.postValue(it)
                    } else {
                        _signUpValidate.postValue(it?.message())
                    }
                }
            }catch (e:IOException){
                _signUpValidate.postValue(e.localizedMessage)
            }
        }
    }

    private fun cacheUserDate(it: Response<UserResponseModel>?) {
        MySharedPreferences.setInShared(USER_TOKEN, it!!.body()!!.token)
        MySharedPreferences.setInShared(USER_NAME,it.body()!!.fullName)
    }

    /*private suspend fun checkIfEmailExists(email: String) :String {
        var response = ""
       val job = viewModelScope.launch(Dispatchers.IO){
              repo.checkIfEmailExists(email)
        }
        job.join()
        return response
    }*/
    private fun checkIfEmailExists(user: UserSignupModel) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.checkIfEmailExists(user.Email) {
                    if (it!!.equals(TRUE)){
                        _signUpValidate.postValue(it)}
                    else if (it!!.equals(FALSE)){
                        _signUpValidate.postValue(it)
                    }}
            }catch (e:IOException){
                e.printStackTrace()
                _signUpValidate.postValue(e.localizedMessage)
            }
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