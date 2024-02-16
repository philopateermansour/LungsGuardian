package com.example.lungsguardian.data.repository

import com.example.lungsguardian.data.model.UserResponseModel
import com.example.lungsguardian.data.model.ResetPasswordModel
import com.example.lungsguardian.data.model.UserLoginModel
import com.example.lungsguardian.data.model.UserSignupModel
import com.example.lungsguardian.data.source.remote.AuthApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class Repo @Inject constructor(val getCalls:AuthApi) : IRepo {

    override suspend fun createAccount(user: UserSignupModel, userCallback: (Response<UserResponseModel>?) -> Unit)

    = withContext(Dispatchers.IO)
    {
        val response = getCalls.createAccount(user)
        userCallback.invoke(response)
    }

    override suspend fun login(user: UserLoginModel, userCallback: (Response<UserResponseModel>?) -> Unit)
            = withContext(Dispatchers.IO)
    {
                val response= getCalls.login(user)
                 userCallback.invoke(response)
    }

    override suspend fun sendCode(email: String, codeCallBack : (Response<String>?)->Unit) = withContext(Dispatchers.IO){

        val response = getCalls.sendCode(email)
        codeCallBack.invoke(response)
    }


    override suspend fun resetPassword(
        resetPasswordModel: ResetPasswordModel,
        resetCallback: (Response<String>?) -> Unit
    )
            = withContext(Dispatchers.IO)
    {
        val response = getCalls.resetPassword(resetPasswordModel.Email, resetPasswordModel)
            resetCallback.invoke(response)
    }

    override suspend fun checkIfEmailExists(email: String, checkCallback: (Response<String>?) -> Unit)
            = withContext(Dispatchers.IO)
    {
        val response = getCalls.checkIfEmailExists(email)
            checkCallback.invoke(response)
    }
}
