package com.notemates.data.repositories

import android.app.Application
import android.util.Log
import arrow.core.Either
import com.notemates.R
import com.notemates.data.models.Note
import com.notemates.data.models.requests.NoteDashboardRequest
import com.notemates.data.sources.remote.NoteApi
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val application: Application,
    private val noteApi: NoteApi,
) {
    companion object {
        private const val TAG = "NoteRepository"
    }

    suspend fun getTrending(): Either<Error, List<Note>> = try {
        val response = noteApi.getTrending()
        if (response.isSuccessful)
            Either.Right(response.body()!!)
        else
            Either.Left(Error(application.getString(R.string.something_went_wrong)))
    } catch (e: Exception) {
        Either.Left(Error(e.message))
    }

    suspend fun getLatest(): Either<Error, List<Note>> = try {
        val response = noteApi.getLatest()
        if (response.isSuccessful)
            Either.Right(response.body()!!)
        else
            Either.Left(Error(application.getString(R.string.something_went_wrong)))
    } catch (e: Exception) {
        Either.Left(Error(e.message))
    }

    suspend fun getDashboard(idRequester: Int): Either<Error, List<Note>> = try {
        val response = noteApi.getDashboard(NoteDashboardRequest(idRequester))
        if (response.isSuccessful)
            Either.Right(response.body()!!)
        else
            Either.Left(Error(application.getString(R.string.something_went_wrong)))
    } catch (e: Exception) {
        Either.Left(Error(e.message))
    }

    suspend fun get(idNote: Int): Either<Error, Note> = try {
        val response = noteApi.get(idNote)
        if (response.isSuccessful)
            Either.Right(response.body()!!)
        else
            Either.Left(Error(application.getString(R.string.something_went_wrong)))
    } catch (e: Exception) {
        Either.Left(Error(e.message))
    }

    suspend fun incrementView(idNote: Int) {
        try {
            noteApi.incrementView(idNote)
        } catch (e: Exception) {
            Log.e(TAG, "incrementView: ${e.message}")
        }
    }
}