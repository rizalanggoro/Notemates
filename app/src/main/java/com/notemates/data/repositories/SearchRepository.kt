package com.notemates.data.repositories

import android.app.Application
import arrow.core.Either
import com.notemates.R
import com.notemates.data.models.responses.SearchResponse
import com.notemates.data.sources.remote.SearchApi
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val application: Application,
    private val searchApi: SearchApi,
) {
    suspend fun search(keyword: String): Either<Error, SearchResponse> {
        try {
            val response = searchApi.search(keyword)
            return if (response.isSuccessful)
                Either.Right(response.body()!!)
            else {
                var errorMessage = R.string.something_went_wrong
                when (response.code()) {
                    400 -> errorMessage = R.string.bad_request
                }
                return Either.Left(Error(application.getString(errorMessage)))
            }
        } catch (e: Exception) {
            return Either.Left(Error(application.getString(R.string.something_went_wrong)))
        }
    }
}