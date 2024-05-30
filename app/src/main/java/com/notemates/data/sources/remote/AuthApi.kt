package com.notemates.data.sources.remote

import com.notemates.data.models.User
import com.notemates.data.models.requests.LoginPayload
import com.notemates.data.models.requests.RegisterPayload
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body payload: LoginPayload): Response<User>

    @POST("auth/register")
    suspend fun register(@Body payload: RegisterPayload): Response<User>
}