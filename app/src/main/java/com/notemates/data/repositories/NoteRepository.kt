package com.notemates.data.repositories

import android.app.Application
import arrow.core.Either
import com.notemates.R
import com.notemates.data.models.Note
import com.notemates.data.sources.remote.NoteApi
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val application: Application,
    private val noteApi: NoteApi,
) {
    suspend fun getLatest(): Either<Error, List<Note>> {
        try {
            val response = noteApi.getLatest()

            return if (response.isSuccessful) {
                Either.Right(response.body()!!)
            } else {
                return Either.Left(Error(application.getString(R.string.something_went_wrong)))
            }
        } catch (e: Exception) {
            return Either.Left(Error(e.message))
        }
    }
}