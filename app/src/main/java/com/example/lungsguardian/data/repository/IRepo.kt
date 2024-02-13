package com.example.lungsguardian.data.repository

import com.example.lungsguardian.data.model.LoginResponse
import com.example.lungsguardian.data.model.ResetPasswordModel
import com.example.lungsguardian.data.model.SignupResponse
import com.example.lungsguardian.data.model.UserLoginModel
import com.example.lungsguardian.data.model.UserSignupModel
import retrofit2.Response

interface IRepo {
    suspend fun createAccount(
        user: UserSignupModel,
        userCallback: (Response<SignupResponse>?) -> Unit
    )

    suspend fun login(user: UserLoginModel, userCallback: (Response<LoginResponse>?) -> Unit)

    suspend fun sendCode(email: String, emailCallback: (Response<String>?) -> Unit)

    suspend fun resetPassword(
        resetPasswordModel: ResetPasswordModel,
        resetCallback: (Response<String>?) -> Unit
    )

    fun checkIfEmailExists(email: String, checkCallback: (Response<String>?) -> Unit)
}