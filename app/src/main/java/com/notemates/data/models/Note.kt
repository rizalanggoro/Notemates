package com.notemates.data.models

data class Note(
    val key: String? = null,
    val userKey: String,
    val title: String,
    val description: String,
    val content: String,
)
