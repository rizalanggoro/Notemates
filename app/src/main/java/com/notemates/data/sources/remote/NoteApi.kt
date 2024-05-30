package com.notemates.data.sources.remote

import com.notemates.data.models.Note
import retrofit2.Response
import retrofit2.http.GET

interface NoteApi {
    @GET("notes/latest")
    suspend fun getLatest(): Response<List<Note>>
}