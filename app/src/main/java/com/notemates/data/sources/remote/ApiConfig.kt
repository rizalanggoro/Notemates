package com.notemates.data.sources.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        private val client = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(
                    HttpLoggingInterceptor.Level.BODY
                )
            )
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder().also {
                        it.addHeader(
                            "X-API-Key",
                            "c0caqzhesqu_M8BozmKcG9oTRvz8G1MJjuQRb59FetnU"
                        )
                        it.addHeader("Content-Type", "application/json")
                    }.build()
                )
            }
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://database.deta.sh/v1/c0caqzhesqu/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}