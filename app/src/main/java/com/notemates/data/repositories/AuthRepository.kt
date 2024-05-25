package com.notemates.data.repositories

import android.app.Application
import androidx.core.content.edit
import arrow.core.Either
import at.favre.lib.crypto.bcrypt.BCrypt
import com.google.gson.Gson
import com.notemates.R
import com.notemates.data.models.User
import com.notemates.data.models.requests.InsertRequest
import com.notemates.data.models.requests.QueryRequest
import com.notemates.data.sources.local.Preferences
import com.notemates.data.sources.remote.UserApi
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val userApi: UserApi,
    private val application: Application,
    private val preferences: Preferences,
) {
    private val prefAuthSessionKey = "auth-session"
    private var _authenticatedUser: User? = null

    val authenticatedUser get() = _authenticatedUser
    val isAuthenticated: Boolean get() = _authenticatedUser != null

    init {
        val string = preferences.instance().getString(prefAuthSessionKey, null)
        if (string != null) {
            _authenticatedUser = Gson().fromJson(string, User::class.java)
        }
    }

    private fun saveUser(user: User) {
        val string = Gson().toJson(user)
        preferences.instance().edit {
            putString(prefAuthSessionKey, string)
            apply()
        }
        _authenticatedUser = user
    }

    fun logout() {
        preferences.instance().edit {
            remove(prefAuthSessionKey)
            apply()
        }
        _authenticatedUser = null
    }

    suspend fun login(
        email: String,
        password: String,
    ): Either<Error, Boolean> {
        if (email.isEmpty())
            return Either.Left(Error(application.getString(R.string.email_validation_empty)))

        if (password.isEmpty())
            return Either.Left(Error(application.getString(R.string.password_validation_empty)))

        val queryResponse = userApi.query(
            QueryRequest(
                query = listOf(mapOf("email" to email)),
                limit = 1,
            )
        )

        if (!queryResponse.isSuccessful)
            return Either.Left(Error(application.getString(R.string.something_went_wrong)))
        
        val queryBody = queryResponse.body()!!
        if (queryBody.paging.size == 0)
            return Either.Left(Error(application.getString(R.string.email_not_found)))

        val firstUser: User = queryBody.items.first()

        // validate password
        if (!BCrypt.verifyer().verify(password.toCharArray(), firstUser.password).verified)
            return Either.Left(Error(application.getString(R.string.invalid_email_or_password)))

        saveUser(firstUser)

        return Either.Right(true)
    }

    suspend fun register(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
    ): Either<Error, Boolean> {
        if (name.isEmpty())
            return Either.Left(Error(application.getString(R.string.name_validation_empty)))

        if (email.isEmpty())
            return Either.Left(Error(application.getString(R.string.email_validation_empty)))

        if (password.isEmpty())
            return Either.Left(Error(application.getString(R.string.password_validation_empty)))

        if (password != confirmPassword)
            return Either.Left(Error(application.getString(R.string.confirm_password_validation_incorrect)))

        val queryResponse = userApi.query(
            QueryRequest(
                query = listOf(mapOf("email" to email)),
                limit = 1,
            )
        )

        if (!queryResponse.isSuccessful)
            return Either.Left(Error(application.getString(R.string.something_went_wrong)))

        val queryBody = queryResponse.body()!!
        if (queryBody.paging.size > 0)
            return Either.Left(Error(application.getString(R.string.email_already_registered)))

        val hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray())
        val insertResponse = userApi.insert(
            InsertRequest(
                item = User(
                    name = name,
                    email = email,
                    password = hashedPassword
                )
            )
        )

        if (!insertResponse.isSuccessful)
            return Either.Left(Error(application.getString(R.string.something_went_wrong)))

        saveUser(insertResponse.body()!!)

        return Either.Right(true)
    }
}