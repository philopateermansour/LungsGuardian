package com.example.lungsguardian.data.source.remote

import com.example.lungsguardian.data.model.LoginResponse
import com.example.lungsguardian.data.model.SignupResponse
import com.example.lungsguardian.data.model.UserLoginModel
import com.example.lungsguardian.data.model.UserSignupModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("Register")
    fun createAccount(
        @Body userSignupModel: UserSignupModel
    ): Call<SignupResponse>

    @POST("Login")
    fun login(
        @Body userLoginModel: UserLoginModel
    ): Call<LoginResponse>
}