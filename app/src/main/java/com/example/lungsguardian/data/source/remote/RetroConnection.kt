package com.example.lungsguardian.data.source.remote

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object RetroConnection {


    var gson = GsonBuilder()
        .setLenient()
        .create()


    val baseURL = "http://192.168.1.6:7178/Api/Accounts/"
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseURL).addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val getCalls = retrofit.create(AuthApi::class.java)
}