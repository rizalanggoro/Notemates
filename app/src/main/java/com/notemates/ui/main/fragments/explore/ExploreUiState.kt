package com.notemates.ui.main.fragments.explore

import com.notemates.core.utils.StateStatus
import com.notemates.data.models.Note

enum class ExploreUiAction {
    Initial,
    GetLatestNotes,
}

data class ExploreUiState(
    val action: ExploreUiAction = ExploreUiAction.Initial,
    val status: StateStatus = StateStatus.Initial,
    val message: String = "",
    val listLatestNotes: List<Note> = listOf(),
)
