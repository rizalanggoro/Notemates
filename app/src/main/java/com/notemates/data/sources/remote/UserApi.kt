package com.notemates.data.sources.remote

import com.notemates.data.models.User
import com.notemates.data.models.requests.UserProfileRequest
import com.notemates.data.models.responses.UserProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {
    @POST("users/profile/{idUser}")
    suspend fun getProfile(
        @Path("idUser") idUser: Int,
        @Body payload: UserProfileRequest,
    ): Response<UserProfileResponse>

    @GET("users/profile/{idUser}/followed-by")
    suspend fun getFollowedBy(@Path("idUser") idUser: Int): Response<List<User>>

    @GET("users/profile/{idUser}/following")
    suspend fun getFollowing(@Path("idUser") idUser: Int): Response<List<User>>
}