package com.notemates.data.sources.remote

import com.notemates.data.models.Note
import com.notemates.data.models.requests.NoteCreatePayload
import com.notemates.data.models.requests.NoteDashboardRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface NoteApi {
    @GET("notes/popular")
    suspend fun getPopular(): Response<List<Note>>

    @GET("notes/latest")
    suspend fun getLatest(): Response<List<Note>>

    @POST("notes/dashboard")
    suspend fun getDashboard(@Body payload: NoteDashboardRequest): Response<List<Note>>

    @GET("notes/{idNote}")
    suspend fun get(
        @Header("idRequester") idRequester: Int,
        @Path("idNote") idNote: Int
    ): Response<Note>

    @GET("notes/{idNote}/like")
    suspend fun like(
        @Header("idRequester") idRequester: Int,
        @Path("idNote") idNote: Int
    ): Response<Any>

    @GET("notes/{idNote}/dislike")
    suspend fun dislike(
        @Header("idRequester") idRequester: Int,
        @Path("idNote") idNote: Int
    ): Response<Any>

    @GET("notes/{idNote}/increment-view")
    suspend fun incrementView(@Path("idNote") idNote: Int): Response<Any>

    @POST("notes")
    suspend fun create(@Body payload: NoteCreatePayload): Response<Note>
}