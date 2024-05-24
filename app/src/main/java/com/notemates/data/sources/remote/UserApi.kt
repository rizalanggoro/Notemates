package com.notemates.data.sources.remote

import com.notemates.data.models.User
import com.notemates.data.models.requests.InsertRequest
import com.notemates.data.models.requests.QueryRequest
import com.notemates.data.models.responses.QueryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("users/items")
    suspend fun insert(@Body payload: InsertRequest<User>): Response<User>

    @POST("users/query")
    suspend fun query(@Body payload: QueryRequest? = QueryRequest()): Response<QueryResponse<User>>
}