package com.notemates.data.models

import com.google.gson.annotations.SerializedName

data class Note(
    val id: Int,
    val title: String,
    val description: String,
    val content: String,
    val authorId: Int, // rename jadi idauthor
    @field:SerializedName("_count") val count: Count,
) {
    data class Count(
        val like: Int,
        val comments: Int,
    )
}
