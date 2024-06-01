package com.notemates.data.models.responses

import com.google.gson.annotations.SerializedName

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

    data class Note(
        val id: Int,
        val title: String,
        val description: String,
        @field:SerializedName("_count") val count: Count,
        val user: User,
    ) {
        data class Count(
            val likes: Int,
            val comments: Int,
        )

        data class User(
            val name: String,
            val email: String,
        )
    }
}
