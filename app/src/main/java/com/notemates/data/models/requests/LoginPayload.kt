package com.notemates.data.models.requests

data class LoginPayload(
    val email: String,
    val password: String,
)