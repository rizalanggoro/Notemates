package com.notemates.data.models.requests

data class NoteCreatePayload(
    val title: String,
    val description: String,
    val content: String,
    val idUser: Int,
)