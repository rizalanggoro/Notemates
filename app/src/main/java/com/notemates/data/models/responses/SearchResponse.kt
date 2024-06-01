package com.notemates.data.models.responses

import com.google.gson.annotations.SerializedName
import com.notemates.data.models.Note

data class SearchResponse(
    val users: List<User> = listOf(),
    val notes: List<Note> = listOf(),
) {
    data class User(
        val id: Int,
        val name: String,
        val email: String,
        @field:SerializedName("_count") val count: Count,
    ) {
        data class Count(
            val followedBy: Int,
        )
    }
}
