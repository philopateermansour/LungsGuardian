package com.example.lungsguardian.ui.auth.forget

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lungsguardian.VALIDATE_EMAIL_INVALID
import com.example.lungsguardian.VALIDATE_EMAIL_NULL

class ForgetViewModel : ViewModel() {
    private val _forgetValidate = MutableLiveData<String>()
    val forgetValidate get() = _forgetValidate
    fun validate(email: String) {
        if (email.isEmpty()) {
            forgetValidate.value = VALIDATE_EMAIL_NULL
        } else if (!isEmailValid(email)) {
            forgetValidate.value = VALIDATE_EMAIL_INVALID
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}