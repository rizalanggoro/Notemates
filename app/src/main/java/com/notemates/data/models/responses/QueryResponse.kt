package com.notemates.data.models.responses

data class QueryResponse<T>(
    val paging: Paging,
    val items: List<T>
)

data class Paging(
    val size: Int,
    val last: String,
)