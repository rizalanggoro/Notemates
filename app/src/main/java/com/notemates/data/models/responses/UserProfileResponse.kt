package com.notemates.data.models.responses

import com.google.gson.annotations.SerializedName
import com.notemates.data.models.Note

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
}
