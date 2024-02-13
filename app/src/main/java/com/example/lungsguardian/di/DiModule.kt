package com.example.lungsguardian.di

import com.example.lungsguardian.data.repository.IRepo
import com.example.lungsguardian.data.repository.Repo
import com.example.lungsguardian.data.source.remote.AuthApi
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiModule {

    var gson = GsonBuilder()
        .setLenient()
        .create()
    val baseURL = "http://192.168.1.6:7178/Api/Accounts/"

    @Singleton
    @Provides
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseURL).addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    @Singleton
    @Provides
    fun getCalls(retrofit: Retrofit):AuthApi{
        return retrofit.create(AuthApi::class.java)
    }
    @Singleton
    @Provides
    fun getIRep(apiCalls: AuthApi): IRepo {
        return Repo(apiCalls)
    }
}