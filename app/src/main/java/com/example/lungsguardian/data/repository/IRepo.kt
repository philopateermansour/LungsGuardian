package com.example.lungsguardian.data.repository

import android.graphics.Bitmap
import com.example.lungsguardian.data.model.MlResponseModel
import com.example.lungsguardian.data.model.ResetPasswordModel
import com.example.lungsguardian.data.model.UserLoginModel
import com.example.lungsguardian.data.model.UserResponseModel
import com.example.lungsguardian.data.model.UserSignupModel
import retrofit2.Response
import java.io.File

interface IRepo {
    suspend fun createAccount(
        user: UserSignupModel,
        userCallback: (Response<UserResponseModel>?) -> Unit
    )

    suspend fun login(user: UserLoginModel, userCallback: (Response<UserResponseModel>?) -> Unit)

    suspend fun sendCode(email: String, codeCallBack: (Response<String>?) -> Unit)

    suspend fun resetPassword(
        resetPasswordModel: ResetPasswordModel,
        resetCallback: (Response<String>?) -> Unit
    )

    /*suspend fun checkIfEmailExists(email: String) :String*/
    suspend fun checkIfEmailExists(email: String, checkCallBack: (String?) -> Unit)
    suspend fun showProfile(userCallback: (Response<UserResponseModel>?) -> Unit)
    suspend fun editName(fullName: String, editCallback: (Response<String>?) -> Unit)
    suspend fun editEmail(email: String, editCallback: (Response<String>?) -> Unit)
    suspend fun changePassword(
        oldPassword: String,
        newPassword: String,
        passwordCallback: (Response<String>?) -> Unit
    )
    suspend fun sendImageToModel(image :Bitmap,modelCallback: (Response<MlResponseModel>?) -> Unit)
}