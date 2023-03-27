package com.example.student_assistant.di.module

import com.example.student_assistant.BuildConfig
import com.example.student_assistant.data.network.AuthApi
import com.example.student_assistant.data.network.ErrorConverterCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {
    @Provides
    fun api(client: OkHttpClient): AuthApi {
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ErrorConverterCallAdapterFactory())
            .baseUrl(BuildConfig.BASE_URL)
            .build()
            .create(AuthApi::class.java)
    }

    @Provides
    fun client(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor {
            val newRequest: Request = it.request().newBuilder()
//                .addHeader("Authorization", "Bearer ${NetworkConstants.TOKEN}")
                .addHeader("Content-Type", "application/json")
                .build()
            it.proceed(newRequest)
        }
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }
}