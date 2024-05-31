package com.notemates.data.sources.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        private const val BASE_URL = "https://notemates-api.vercel.app"
//        private const val BASE_URL =
//            "https://758f-2001-448a-4027-1e11-c446-d811-356f-b4eb.ngrok-free.app"

        private val client = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(
                    HttpLoggingInterceptor.Level.BODY
                )
            )
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("$BASE_URL/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}