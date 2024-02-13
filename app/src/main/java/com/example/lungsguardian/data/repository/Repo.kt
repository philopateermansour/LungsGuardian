package com.example.lungsguardian.data.repository

import com.example.lungsguardian.data.model.LoginResponse
import com.example.lungsguardian.data.model.ResetPasswordModel
import com.example.lungsguardian.data.model.SignupResponse
import com.example.lungsguardian.data.model.UserLoginModel
import com.example.lungsguardian.data.model.UserSignupModel
import com.example.lungsguardian.data.source.remote.AuthApi
import retrofit2.Response

class Repo constructor(val getCalls:AuthApi) : IRepo {

    override suspend fun createAccount(user: UserSignupModel, userCallback: (Response<SignupResponse>?) -> Unit) {
        val response = getCalls.createAccount(user)
        userCallback.invoke(response)
    }

    override suspend fun login(user: UserLoginModel, userCallback: (Response<LoginResponse>?) -> Unit) {
        val response = getCalls.login(user)
        userCallback.invoke(response)
    }

    override suspend fun sendCode(email: String, emailCallback: (Response<String>?) -> Unit) {
        val response = getCalls.sendCode(email)
        emailCallback.invoke(response)
    }

    override suspend fun resetPassword(
        resetPasswordModel: ResetPasswordModel,
        resetCallback: (Response<String>?) -> Unit
    ) {
        val response = getCalls.resetPassword(resetPasswordModel.Email, resetPasswordModel)
            resetCallback.invoke(response)
    }

    override  fun checkIfEmailExists(email: String, checkCallback: (Response<String>?) -> Unit) {
        val response = getCalls.checkIfEmailExists(email)
            checkCallback.invoke(response)
    }
}


















