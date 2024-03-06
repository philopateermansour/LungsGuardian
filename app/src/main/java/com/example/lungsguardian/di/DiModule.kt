package com.example.lungsguardian.di

import com.bumptech.glide.provider.ResourceEncoderRegistry
import com.example.lungsguardian.data.repository.IRepo
import com.example.lungsguardian.data.repository.Repo
import com.example.lungsguardian.data.source.remote.AuthApi
import com.example.lungsguardian.data.source.remote.MlApi
import com.example.lungsguardian.utils.BASE_URL_BACK
import com.example.lungsguardian.utils.BASE_URL_ML
import com.example.lungsguardian.utils.MySharedPreferences
import com.example.lungsguardian.utils.RETROFIT_FOR_BACK
import com.example.lungsguardian.utils.RETROFIT_FOR_ML
import com.example.lungsguardian.utils.USER_TOKEN
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiModule {

    @Singleton
    @Provides
    @Named(RETROFIT_FOR_BACK)
    fun getRetrofitForBack(): Retrofit {
        val client = OkHttpClient.Builder()
            .connectTimeout(50, TimeUnit.SECONDS)
            .writeTimeout(150, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .callTimeout(50, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val originalRequest = chain.request()
                    val originalUrl = originalRequest.url
                    val url = originalUrl.newBuilder().build()
                    val requestBuilder = originalRequest.newBuilder().url(url)
                        .addHeader("Accept", "application/json") // Accept header for XML data
                        .addHeader("Authorization", "Bearer ${MySharedPreferences.getFromShared(USER_TOKEN)}") // Authorization header with Bearer token
                        .method(originalRequest.method, originalRequest.body) // Set method and request body

                    val request = requestBuilder.build()
                    val response = chain.proceed(request)
                    response.code //status code
                    return response
                }
            })
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL_BACK)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                .setLenient()
                .create()))
            .build()
    }
    @Singleton
    @Provides
    @Named(RETROFIT_FOR_ML)
    fun getRetrofitForMl(): Retrofit {
        val client = OkHttpClient.Builder()
            .connectTimeout(50, TimeUnit.SECONDS)
            .writeTimeout(150, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .callTimeout(50, TimeUnit.SECONDS).build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL_ML)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                .setLenient()
                .create()))
            .build()
    }
    @Singleton
    @Provides
    fun getCalls(@Named(RETROFIT_FOR_BACK) retrofit: Retrofit):AuthApi{
        return retrofit.create(AuthApi::class.java)
    }

    @Singleton
    @Provides
    fun getMlCall(@Named(RETROFIT_FOR_ML) retrofit: Retrofit) :MlApi{
        return retrofit.create(MlApi::class.java)
    }

    @Singleton
    @Provides
    fun getIRep(apiCalls: AuthApi,mlApi:MlApi): IRepo {
        return Repo(apiCalls,mlApi)
    }
}