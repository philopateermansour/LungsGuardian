package com.example.lungsguardian.data.source.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroConnection {

    val baseURL = "http://192.168.1.6:7178/Api/Accounts/"
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val getCalls = retrofit.create(AuthApi::class.java)
}