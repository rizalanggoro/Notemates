package com.notemates.data.models

import com.google.gson.annotations.SerializedName

data class Note(
    val id: Int,
    val title: String,
    val description: String,
    val content: String,
    val views: Int,
    @field:SerializedName("_count") val count: Count,
    val user: User,
    val isLiked: Boolean,
) {
    data class Count(
        val likes: Int,
    )

    data class User(
        val name: String,
    )
}
