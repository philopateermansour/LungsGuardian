package com.example.lungsguardian.ui.auth.forget

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lungsguardian.VALIDATE_EMAIL_PROBLEM

class ForgetViewModel : ViewModel() {
    private val _forgetValidate = MutableLiveData<String>()
    val forgetValidate get() = _forgetValidate
    fun validate(email: String) {
        if (email.isEmpty()) {
            forgetValidate.value = VALIDATE_EMAIL_PROBLEM
        }
    }
}