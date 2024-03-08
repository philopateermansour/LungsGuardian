package com.example.lungsguardian.data.source.remote

import com.example.lungsguardian.data.model.PredictionModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MlApi {


    @Multipart
    @POST("predict")
    suspend fun sendImageToMl(
        @Part image :MultipartBody.Part
    ): Response<PredictionModel>

}