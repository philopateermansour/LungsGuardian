package com.example.lungsguardian.data.source.remote

import com.example.lungsguardian.data.model.LoginResponse
import com.example.lungsguardian.data.model.ResetPasswordModel
import com.example.lungsguardian.data.model.SignupResponse
import com.example.lungsguardian.data.model.UserLoginModel
import com.example.lungsguardian.data.model.UserSignupModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
    @POST("Register")
    fun createAccount(
        @Body userSignupModel: UserSignupModel
    ): Call<SignupResponse>

    @POST("Login")
    fun login(
        @Body userLoginModel: UserLoginModel
    ): Call<LoginResponse>

    @POST("sendemail")
    fun sendCode(
        @Query("Email") email: String
    ): Call<String>

    @POST("ResetPassword")
    fun resetPassword(
        @Query("Email") email: String,@Body resetPasswordModel: ResetPasswordModel
    ): Call<String>
}