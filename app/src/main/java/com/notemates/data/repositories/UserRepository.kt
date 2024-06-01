package com.notemates.data.repositories

import android.app.Application
import arrow.core.Either
import com.notemates.R
import com.notemates.data.models.requests.UserProfileRequest
import com.notemates.data.models.responses.UserProfileResponse
import com.notemates.data.sources.remote.UserApi
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val application: Application,
    private val userApi: UserApi,
) {
    suspend fun getProfile(
        idRequester: Int,
        idUser: Int,
    ): Either<Error, UserProfileResponse> = try {
        val response = userApi.getProfile(
            idUser, UserProfileRequest(
                idRequester
            )
        )
        if (response.isSuccessful)
            Either.Right(response.body()!!)
        else
            Either.Left(Error(application.getString(R.string.something_went_wrong)))
    } catch (e: Exception) {
        Either.Left(Error(e.message))
    }
}