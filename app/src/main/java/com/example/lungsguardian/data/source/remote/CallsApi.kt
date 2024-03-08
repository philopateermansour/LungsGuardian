package com.example.lungsguardian.data.source.remote

import com.example.lungsguardian.data.model.ChangePasswordModel
import com.example.lungsguardian.data.model.CurrentUserDataModel
import com.example.lungsguardian.data.model.Email
import com.example.lungsguardian.data.model.HistoryModel
import com.example.lungsguardian.data.model.Name
import com.example.lungsguardian.data.model.PredictionModel
import com.example.lungsguardian.data.model.UserResponseModel
import com.example.lungsguardian.data.model.ResetPasswordModel
import com.example.lungsguardian.data.model.UploadImageResponseModel
import com.example.lungsguardian.data.model.UserLoginModel
import com.example.lungsguardian.data.model.UserSignupModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface CallsApi {
    @POST("Register")
    suspend fun createAccount(
        @Body userSignupModel: UserSignupModel
    ): Response<UserResponseModel>

    @POST("Login")
    suspend fun login(
        @Body userLoginModel: UserLoginModel
    ): Response<UserResponseModel>

    @POST("sendemail")
    suspend fun sendCode(
        @Query("Email") email: String
    ): Response<String>

    @POST("ResetPassword")
    suspend fun resetPassword(
        @Body resetPasswordModel: ResetPasswordModel
    ): Response<String>

    @GET("emailExists")
    suspend  fun  checkIfEmailExists(
        @Query("Email") email: String
    ):String

    @GET("CurrentUser")
    suspend fun  showProfile() :Response<CurrentUserDataModel>

    @PUT("EditProfile")
    suspend fun editName(
        @Body fullName: Name
    ) :Response<String>

    @PUT("EditProfile")
    suspend fun editEmail(
        @Body email: Email
    ) :Response<String>

    @PUT("ChangePassword")
    suspend fun changePassword(
       @Body changePasswordModel:ChangePasswordModel
    ):Response<String>

    @Multipart
    @POST("SendImageAndPredict")
    suspend fun sendImageToMl(
        @Part image : MultipartBody.Part
    ): Response<String>

    @GET("PredictionHistory")
    suspend fun showHistory(

    ):Response<HistoryModel>


    @DELETE("DeletePrediction")
    suspend fun deleteReport(
        @Query("predictionId") predictionId:Int
    ):Response<String>

    @Multipart
    @POST("UploadImage")
    suspend fun uploadProfileImage(
        @Part imageFile:MultipartBody.Part
    ):Response<UploadImageResponseModel>

    @DELETE("DeleteImage")
    suspend fun deleteProfileImage():Response<String>
}