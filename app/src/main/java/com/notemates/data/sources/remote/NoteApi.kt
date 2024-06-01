package com.notemates.data.sources.remote

import com.notemates.data.models.Note
import com.notemates.data.models.requests.NoteDashboardRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NoteApi {
    @GET("notes/trending")
    suspend fun getTrending(): Response<List<Note>>

    @GET("notes/latest")
    suspend fun getLatest(): Response<List<Note>>

    @POST("notes/dashboard")
    suspend fun getDashboard(@Body payload: NoteDashboardRequest): Response<List<Note>>

    @GET("notes/{idNote}")
    suspend fun get(@Path("idNote") idNote: Int): Response<Note>

    @GET("notes/{idNote}/increment-view")
    suspend fun incrementView(@Path("idNote") idNote: Int): Response<Any>
}