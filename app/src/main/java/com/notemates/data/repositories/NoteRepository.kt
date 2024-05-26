package com.notemates.data.repositories

import android.app.Application
import arrow.core.Either
import com.notemates.R
import com.notemates.data.models.Note
import com.notemates.data.models.requests.InsertRequest
import com.notemates.data.models.requests.QueryRequest
import com.notemates.data.sources.remote.NoteApi
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val application: Application,
    private val noteApi: NoteApi,
) {
    suspend fun publish(
        userKey: String,
        title: String,
        description: String,
        content: String,
    ): Either<Error, Boolean> {
        if (title.isEmpty())
            return Either.Left(Error(application.getString(R.string.title_validation_empty)))

        if (description.isEmpty())
            return Either.Left(Error(application.getString(R.string.description_validation_empty)))

        if (content.isEmpty())
            return Either.Left(Error(application.getString(R.string.content_validation_empty)))

        // todo: butuh validasi judul sebelum insert

        val insertResponse = noteApi.insert(
            InsertRequest(
                item = Note(
                    key = System.currentTimeMillis().toString(),
                    userKey = userKey,
                    title = title,
                    description = description,
                    content = content,
                )
            )
        )

        if (!insertResponse.isSuccessful)
            return Either.Left(Error(application.getString(R.string.something_went_wrong)))

        return Either.Right(true)
    }

    suspend fun getLatestNotes(): Either<Error, List<Note>> {
        val queryResponse = noteApi.query(
            QueryRequest(
                query = listOf(),
                limit = 5,
                sort = "desc",
            )
        )

        if (!queryResponse.isSuccessful)
            return Either.Left(Error(application.getString(R.string.something_went_wrong)))

        return Either.Right(queryResponse.body()!!.items)
    }
}