package com.notemates.data.models.responses

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    val id: Int,
    val name: String,
    val email: String,
    @field:SerializedName("_count") val count: Count,
    val notes: List<Note>,
    val isFollowed: Boolean,
) {
    data class Count(
        val followedBy: Int,
        val following: Int,
        val notes: Int,
    )

    data class Note(
        val id: Int,
        val title: String,
        val description: String,
        val views: Int,
        @field:SerializedName("_count") val count: Count,
    ) {
        data class Count(
            val comments: Int,
            val likes: Int,
        )
    }
}
