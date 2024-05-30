package com.notemates.ui.search

import com.notemates.core.utils.StateStatus
import com.notemates.data.models.Note
import com.notemates.data.models.User

enum class SearchUiAction {
    Initial, Search,
}

data class SearchUiState(
    val action: SearchUiAction = SearchUiAction.Initial,
    val status: StateStatus = StateStatus.Initial,
    val message: String = "",
    val users: List<User> = listOf(),
    val notes: List<Note> = listOf(),
)
