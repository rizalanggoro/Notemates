package com.notemates.data.models.requests

data class QueryRequest(
    val query: List<Map<String, Any>>? = null,
    val limit: Int? = null,
    val last: String? = null,
    val sort: String? = null,
)
