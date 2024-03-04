package com.example.lungsguardian.data.source.remote

import android.graphics.Bitmap
import com.example.lungsguardian.data.model.ChangePasswordModel
import com.example.lungsguardian.data.model.MlResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.PUT
import java.io.File

interface MlApi {


    @FormUrlEncoded
    @POST("predict")
    suspend fun sendImageToMl(
        @Field ("image") image: Bitmap
    ): Response<MlResponseModel>
}