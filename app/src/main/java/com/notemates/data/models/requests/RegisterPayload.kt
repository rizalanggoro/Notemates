package com.notemates.data.models.requests

data class RegisterPayload(
    val name: String,
    val email: String,
    val password: String,
)
