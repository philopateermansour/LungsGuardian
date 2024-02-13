package com.example.lungsguardian.data.source.remote

import com.example.lungsguardian.data.model.LoginResponse
import com.example.lungsguardian.data.model.ResetPasswordModel
import com.example.lungsguardian.data.model.SignupResponse
import com.example.lungsguardian.data.model.UserLoginModel
import com.example.lungsguardian.data.model.UserSignupModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
    @POST("Register")
    suspend fun createAccount(
        @Body userSignupModel: UserSignupModel
    ): Response<SignupResponse>

    @POST("Login")
    suspend fun login(
        @Body userLoginModel: UserLoginModel
    ): Response<LoginResponse>

    @POST("sendemail")
    suspend fun sendCode(
        @Query("Email") email: String
    ): Response<String>

    @POST("ResetPassword")
    suspend fun resetPassword(
        @Query("Email") email: String,@Body resetPasswordModel: ResetPasswordModel
    ): Response<String>


    @GET("emailExists")
    fun  checkIfEmailExists(
        @Query("Email") email: String
    ):Response<String>
}