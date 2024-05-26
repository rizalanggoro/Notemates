package com.notemates.data.sources.remote

import com.notemates.data.models.Note
import com.notemates.data.models.requests.InsertRequest
import com.notemates.data.models.requests.QueryRequest
import com.notemates.data.models.responses.QueryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NoteApi {
    @POST("notes/items")
    suspend fun insert(@Body payload: InsertRequest<Note>): Response<Note>

    @POST("notes/query")
    suspend fun query(@Body payload: QueryRequest): Response<QueryResponse<Note>>
}