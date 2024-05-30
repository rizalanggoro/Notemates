package com.notemates.data.repositories

import android.app.Application
import androidx.core.content.edit
import arrow.core.Either
import com.google.gson.Gson
import com.notemates.R
import com.notemates.data.models.User
import com.notemates.data.models.requests.LoginPayload
import com.notemates.data.models.requests.RegisterPayload
import com.notemates.data.sources.local.Preferences
import com.notemates.data.sources.remote.AuthApi
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val application: Application,
    private val authApi: AuthApi,
    private val preferences: Preferences,
) {
    private val prefAuthSessionKey = "auth-session"
    private var _authenticatedUser: User? = null
    val authenticatedUser get() = _authenticatedUser

    init {
        val sessionString = preferences.instance().getString(prefAuthSessionKey, null)
        if (sessionString != null) {
            _authenticatedUser = Gson().fromJson(sessionString, User::class.java)
        }
    }

    private fun saveUser(user: User) = preferences.instance().edit {
        putString(prefAuthSessionKey, Gson().toJson(user))
        apply()
    }.also {
        _authenticatedUser = user
    }

//    private fun saveUser(user: User) {}

    fun logout() = preferences.instance().edit {
        remove(prefAuthSessionKey)
        apply()
    }.also {
        _authenticatedUser = null
    }

    suspend fun login(
        email: String,
        password: String
    ): Either<Error, Boolean> {
        try {
            if (email.isEmpty())
                return Either.Left(Error(application.getString(R.string.email_validation_empty)))

            if (password.isEmpty())
                return Either.Left(Error(application.getString(R.string.password_validation_empty)))

            val response = authApi.login(LoginPayload(email, password))

            return if (response.isSuccessful) {
                saveUser(response.body()!!)
                Either.Right(true)
            } else {
                var errorMessage = R.string.something_went_wrong
                when (response.code()) {
                    404 -> errorMessage = R.string.email_not_found
                    401 -> errorMessage = R.string.invalid_email_or_password
                }
                Either.Left(Error(application.getString(errorMessage)))
            }
        } catch (e: Exception) {
            return Either.Left(Error(e.message))
        }
    }

    suspend fun register(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
    ): Either<Error, Boolean> {
        try {
            if (name.isEmpty())
                return Either.Left(Error(application.getString(R.string.name_validation_empty)))

            if (email.isEmpty())
                return Either.Left(Error(application.getString(R.string.email_validation_empty)))

            if (password.isEmpty())
                return Either.Left(Error(application.getString(R.string.password_validation_empty)))

            if (password != confirmPassword)
                return Either.Left(Error(application.getString(R.string.confirm_password_validation_incorrect)))

            val response = authApi.register(RegisterPayload(name, email, password))

            return if (response.isSuccessful) {
                saveUser(response.body()!!)
                Either.Right(true)
            } else {
                var errorMessage = R.string.something_went_wrong
                when (response.code()) {
                    409 -> errorMessage = R.string.email_already_registered
                }
                Either.Left(Error(application.getString(errorMessage)))
            }
        } catch (e: Exception) {
            return Either.Left(Error(e.message))
        }
    }
}