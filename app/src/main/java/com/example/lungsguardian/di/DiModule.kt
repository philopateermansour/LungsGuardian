package com.example.lungsguardian.di

import com.example.lungsguardian.data.repository.IRepo
import com.example.lungsguardian.data.repository.Repo
import com.example.lungsguardian.data.source.remote.AuthApi
import com.example.lungsguardian.utils.BASE_URL
import com.example.lungsguardian.utils.MySharedPreferences
import com.example.lungsguardian.utils.USER_TOKEN
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiModule {




    @Singleton
    @Provides
    fun getRetrofit(): Retrofit {
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
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Content-Type", "application/xml")
                        .addHeader("Content-Type", "text/plain")
                        .addHeader("Content-Type", "multipart/form-data")
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("Content-Type", "image/jpeg")
                        .addHeader("Content-Type", "image/png")
                        .addHeader("Content-Type", "image/gif")
                        .addHeader("Content-Type", "audio/mpeg")
                        .addHeader("Content-Type", "audio/wav")
                        .addHeader("Content-Type", "audio/ogg")
                        .addHeader("Content-Type", "video/mp4")
                        .addHeader("Content-Type", "video/ogg")
                        .addHeader("Content-Type", "video/webm")
                        .addHeader("Content-Type", "application/octet-stream")
                        // Content-Type header for XML data
                        .method(originalRequest.method, originalRequest.body) // Set method and request body

                    val request = requestBuilder.build()
                    val response = chain.proceed(request)
                    response.code //status code
                    return response
                }
            })
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                .setLenient()
                .create()))
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