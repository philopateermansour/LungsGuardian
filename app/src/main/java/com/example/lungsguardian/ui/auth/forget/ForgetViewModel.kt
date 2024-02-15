package com.example.lungsguardian.ui.auth.forget

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lungsguardian.utils.VALIDATE_EMAIL_INVALID
import com.example.lungsguardian.utils.VALIDATE_EMAIL_NULL
import com.example.lungsguardian.data.repository.IRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ForgetViewModel @Inject constructor(private val repo: IRepo) : ViewModel() {
    private val _forgetValidate = MutableLiveData<String>()
    val forgetValidate get() = _forgetValidate
    private val _sendCodeResponse = MutableLiveData<Response<String>>()
    val sendCodeResponse get() = _sendCodeResponse

    fun validate(email: String) {
        if (email.isEmpty()) {
            _forgetValidate.value = VALIDATE_EMAIL_NULL
        } else if (!isEmailValid(email)) {
            _forgetValidate.value = VALIDATE_EMAIL_INVALID
        } else {
            sendCode(email)
        }
    }

    fun sendCode(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                 repo.sendCode(email){
                    sendCodeResponse.postValue(it)
                }
            } catch (e: IOException) {
                _forgetValidate.postValue(e.localizedMessage)
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}