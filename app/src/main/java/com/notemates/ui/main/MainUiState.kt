package com.notemates.ui.main

import com.notemates.core.utils.StateStatus
import com.notemates.data.models.Note
import com.notemates.data.models.User

enum class MainUiAction {
    Initial, Search
}

data class MainUiState(
    val action: MainUiAction = MainUiAction.Initial,
    val status: StateStatus = StateStatus.Initial,
    val message: String = "",
    var searchUsers: List<User> = listOf(),
    val searchNotes: List<Note> = listOf(),
)
