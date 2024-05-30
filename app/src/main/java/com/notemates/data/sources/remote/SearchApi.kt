package com.notemates.data.sources.remote

import com.notemates.data.models.responses.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("search")
    suspend fun search(@Query("keyword") keyword: String): Response<SearchResponse>
}