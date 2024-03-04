package com.example.lungsguardian.data.source.remote

import android.graphics.Bitmap
import android.net.Uri
import com.example.lungsguardian.data.model.ChangePasswordModel
import com.example.lungsguardian.data.model.MlResponseModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import java.io.File

interface MlApi {


    @Multipart
    @POST("predict")
    suspend fun sendImageToMl(
        @Part image :MultipartBody.Part
    ): Response<MlResponseModel>

}