package com.notemates.data.models.responses

import com.notemates.data.models.Note
import com.notemates.data.models.User

data class SearchResponse(
    val users: List<User>,
    val notes: List<Note>,
)
