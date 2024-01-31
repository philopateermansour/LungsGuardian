package com.example.lungsguardian.ui.auth.forget

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lungsguardian.VALIDATE_EMAIL_INVALID
import com.example.lungsguardian.VALIDATE_EMAIL_NULL
import com.example.lungsguardian.data.repository.Repo
import retrofit2.Response

class ForgetViewModel : ViewModel() {
    private val _forgetValidate = MutableLiveData<String>()
    val forgetValidate get() = _forgetValidate
    private val _sendCodeResponse = MutableLiveData<Response<String>>()
    val sendCodeResponse get() = _sendCodeResponse

    private var repo: Repo = Repo()
    fun validate(email: String) {
        if (email.isEmpty()) {
            forgetValidate.value = VALIDATE_EMAIL_NULL
        } else if (!isEmailValid(email)) {
            forgetValidate.value = VALIDATE_EMAIL_INVALID
        }
        else {
            sendCode(email)
        }
    }
    fun sendCode(email: String){
        repo.sendCode(email){
            sendCodeResponse.value=it
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}