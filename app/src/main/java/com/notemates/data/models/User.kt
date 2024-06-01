package com.notemates.data.models

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    @field:SerializedName("_count") val count: Count,
) {
    data class Count(
        val following: Int,
        val follower: Int,
    )
}
