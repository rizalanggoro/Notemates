package com.notemates.data.models

data class Note(
    val id: Int,
    val title: String,
    val description: String,
    val content: String,
    val authorId: Int, // rename jadi idauthor
    val like: Int,
    val view: Int,
)
