package com.example.lungsguardian.data.repository

import com.example.lungsguardian.data.model.UserResponseModel
import com.example.lungsguardian.data.model.ResetPasswordModel
import com.example.lungsguardian.data.model.UserLoginModel
import com.example.lungsguardian.data.model.UserSignupModel
import retrofit2.Response

interface IRepo {
    suspend fun createAccount(
        user: UserSignupModel,
        userCallback: (Response<UserResponseModel>?) -> Unit
    )

    suspend fun login(user: UserLoginModel, userCallback: (Response<UserResponseModel>?) -> Unit)

    suspend fun sendCode(email: String,codeCallBack :(Response<String>?)-> Unit)

    suspend fun resetPassword(
        resetPasswordModel: ResetPasswordModel,
        resetCallback: (Response<String>?) -> Unit
    )


    /*suspend fun checkIfEmailExists(email: String) :String*/
    suspend fun checkIfEmailExists(email: String,checkCallBack :(String?)->Unit)

    suspend fun showProfile(userCallback: (Response<UserResponseModel>?) -> Unit)

    suspend fun editProfile(email: String,fullName:String, editCallback: (Response<String>?) -> Unit)

    suspend fun changePassword(oldPassword: String,newPassword: String,email: String, passwordCallback: (Response<String>?) -> Unit)
}