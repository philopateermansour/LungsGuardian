package com.example.lungsguardian.data.repository

import com.example.lungsguardian.data.model.CurrentUserDataModel
import com.example.lungsguardian.data.model.HistoryModel
import com.example.lungsguardian.data.model.PredictionModel
import com.example.lungsguardian.data.model.ResetPasswordModel
import com.example.lungsguardian.data.model.UploadImageResponseModel
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
    suspend fun showProfile(userCallback: (Response<CurrentUserDataModel>?) -> Unit)
    suspend fun editName(fullName: String, editCallback: (Response<String>?) -> Unit)
    suspend fun editEmail(email: String, editCallback: (Response<String>?) -> Unit)
    suspend fun changePassword(
        oldPassword: String,
        newPassword: String,
        passwordCallback: (Response<String>?) -> Unit
    )
    suspend fun sendImageToModel(file :File,modelCallback: (Response<PredictionModel>?) -> Unit)
    suspend fun showHistory(historyCallback: (Response<HistoryModel>?) -> Unit)
    suspend fun deleteReports( deleteCallback: (Response<String>?) -> Unit)
    suspend fun uploadProfileImage(file :File, uploadCallBack :(Response<UploadImageResponseModel>?)->Unit)
    suspend fun deleteProfileImage(deleteCallBAck :(Response<String>?)->Unit)
    suspend fun sendCodeToConfirm(email: String,callBAck :(Response<String>?)->Unit)
    suspend fun confirmEmail(email: String, code:String, callBAck :(Response<String>?)->Unit)
}