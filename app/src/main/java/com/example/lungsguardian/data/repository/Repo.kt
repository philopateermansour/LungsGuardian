package com.example.lungsguardian.data.repository

import com.example.lungsguardian.data.model.ChangePasswordModel
import com.example.lungsguardian.data.model.ConfirmEmailModel
import com.example.lungsguardian.data.model.CurrentUserDataModel
import com.example.lungsguardian.data.model.Email
import com.example.lungsguardian.data.model.HistoryModel
import com.example.lungsguardian.data.model.Name
import com.example.lungsguardian.data.model.PredictionModel
import com.example.lungsguardian.data.model.ResetPasswordModel
import com.example.lungsguardian.data.model.UploadImageResponseModel
import com.example.lungsguardian.data.model.UserLoginModel
import com.example.lungsguardian.data.model.UserResponseModel
import com.example.lungsguardian.data.model.UserSignupModel
import com.example.lungsguardian.data.source.remote.CallsApi
import com.example.lungsguardian.data.source.remote.MlApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class Repo @Inject constructor(private val getCalls: CallsApi, private val getMl :MlApi) : IRepo {

    override suspend fun createAccount(
        user: UserSignupModel, userCallback: (Response<UserResponseModel>?) -> Unit
    ) = withContext(Dispatchers.IO) {
        val response = getCalls.createAccount(user)
        userCallback.invoke(response)
    }

    override suspend fun login(
        user: UserLoginModel, userCallback: (Response<UserResponseModel>?) -> Unit
    ) = withContext(Dispatchers.IO) {
        val response = getCalls.login(user)
        userCallback.invoke(response)
    }

    override suspend fun sendCode(email: String, codeCallBack: (Response<String>?) -> Unit) =
        withContext(Dispatchers.IO) {

            val response = getCalls.sendCode(email)
            codeCallBack.invoke(response)
        }


    override suspend fun resetPassword(
        resetPasswordModel: ResetPasswordModel, resetCallback: (Response<String>?) -> Unit
    ) = withContext(Dispatchers.IO) {
        val response = getCalls.resetPassword(resetPasswordModel)
        resetCallback.invoke(response)
    }

    override suspend fun checkIfEmailExists(
        email: String,
        checkCallBack: (String?) -> Unit
    ){
        val response = getCalls.checkIfEmailExists(email)
        checkCallBack.invoke(response)
    }

    /*override suspend fun checkIfEmailExists(
        email: String
    ) = withContext(Dispatchers.IO) {
        getCalls.checkIfEmailExists(email)
    }*/

    override suspend fun showProfile(userCallback: (Response<CurrentUserDataModel>?) -> Unit) =
        withContext(Dispatchers.IO) {
            val response = getCalls.showProfile()
            userCallback.invoke(response)
        }

    override suspend fun editName(fullName: String, editCallback: (Response<String>?) -> Unit)
    = withContext(Dispatchers.IO)
    {
        val response = getCalls.editName(Name(fullName))
        editCallback.invoke(response)
    }

    override suspend fun editEmail(email: String, editCallback: (Response<String>?) -> Unit)
            = withContext(Dispatchers.IO)
    {
        val response = getCalls.editEmail(Email(email))
        editCallback.invoke(response)
    }


    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String,
        passwordCallback: (Response<String>?) -> Unit
    )
            = withContext(Dispatchers.IO){
        val response = getCalls.changePassword(ChangePasswordModel(oldPassword,newPassword))
        passwordCallback.invoke(response)
    }

    override suspend fun sendImageToModel(
        file: File,
        modelCallback: (Response<PredictionModel>?) -> Unit
    )
            = withContext(Dispatchers.IO)
    {
        val response = getCalls.sendImageToMl(
            image = MultipartBody.Part.createFormData(
                "image",file.name,file.asRequestBody()
            )
        )
        modelCallback.invoke(response)
    }

    override suspend fun showHistory(historyCallback: (Response<HistoryModel>?) -> Unit)
    = withContext(Dispatchers.IO) {
        val response = getCalls.showHistory()
        historyCallback.invoke(response)
    }

    override suspend fun deleteReports(deleteCallback: (Response<String>?) -> Unit)  = withContext(Dispatchers.IO)
    {
        val response = getCalls.deleteReports()
        deleteCallback.invoke(response)
    }

    override suspend fun uploadProfileImage(
        file: File,
        uploadCallBack: (Response<UploadImageResponseModel>?) -> Unit
    )    = withContext(Dispatchers.IO)
    {
        val response = getCalls.uploadProfileImage(
            imageFile = MultipartBody.Part.createFormData(
                "imageFile",file.name,file.asRequestBody()
            )
        )
        uploadCallBack.invoke(response)
    }

    override suspend fun deleteProfileImage(deleteCallBAck: (Response<String>?) -> Unit) =
        withContext(Dispatchers.IO) {
        val response = getCalls.deleteProfileImage()
            deleteCallBAck.invoke(response)
    }
    override suspend fun sendCodeToConfirm(email: String, callBAck: (Response<String>?) -> Unit) =
        withContext(Dispatchers.IO){
            val response = getCalls.sendCodeToConfirm(email)
            callBAck.invoke(response)
        }

    override suspend fun confirmEmail(
        email: String,
        code: String,
        callBAck: (Response<String>?) -> Unit
    ) = withContext(Dispatchers.IO){
        val response=getCalls.confirmEmail(ConfirmEmailModel(code,email))
        callBAck.invoke(response)
    }
}
