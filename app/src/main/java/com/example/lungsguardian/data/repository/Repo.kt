package com.example.lungsguardian.data.repository

import android.util.Log
import com.example.lungsguardian.data.model.LoginResponse
import com.example.lungsguardian.data.model.ResetPasswordModel
import com.example.lungsguardian.data.model.SignupResponse
import com.example.lungsguardian.data.model.UserLoginModel
import com.example.lungsguardian.data.model.UserSignupModel
import com.example.lungsguardian.data.source.remote.RetroConnection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repo {

    fun createAccount(user: UserSignupModel, userCallback: (Response<SignupResponse>?) -> Unit) {
        RetroConnection.getCalls.createAccount(user)
            .enqueue(object : Callback<SignupResponse> {
                override fun onResponse(
                    call: Call<SignupResponse>,
                    response: Response<SignupResponse>
                ) {
                    userCallback.invoke(response)
                }

                override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                    Log.e("TAG", "onFailure: ")
                }

            })
    }

    fun login(user: UserLoginModel, userCallback: (Response<LoginResponse>?) -> Unit) {
        RetroConnection.getCalls.login(user)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    userCallback.invoke(response)
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("TAG", "onFailure: ")
                }
            }
            )
    }

    fun sendCode(email: String, emailCallback: (Response<String>?) -> Unit) {
        RetroConnection.getCalls.sendCode(email)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    emailCallback.invoke(response)
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("TAG", "onFailure: ")
                }

            })
    }

    fun resetPassword(
        resetPasswordModel: ResetPasswordModel,
        resetCallback: (Response<String>?) -> Unit
    ) {
        RetroConnection.getCalls.resetPassword(resetPasswordModel.Email, resetPasswordModel)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    resetCallback.invoke(response)
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("TAG", "onFailure: ")
                }
            })
    }

    fun checkIfEmailExists(email: String, checkCallback: (Response<String>?) -> Unit) {
        RetroConnection.getCalls.checkIfEmailExists(email)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    checkCallback.invoke(response)
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("TAG", "onFailure: ")
                }

            })
    }
}


















