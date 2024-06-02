package com.notemates.ui.main.fragments.explore

import com.notemates.core.utils.StateStatus
import com.notemates.data.models.Note

data class ExploreUiState(
    val action: Action = Action.Initial,
    val status: StateStatus = StateStatus.Initial,
    val message: String = "",
    val popularNotes: List<Note> = listOf(),
    val latestNotes: List<Note> = listOf(),
) {
    enum class Action {
        Initial,
        GetPopular,
        GetLatest,
    }
}
