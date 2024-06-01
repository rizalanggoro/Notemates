package com.notemates.ui.main.fragments.explore

import com.notemates.core.utils.StateStatus
import com.notemates.data.models.Note

data class ExploreUiState(
    val status: StateStatus = StateStatus.Initial,
    val message: String = "",
    val notes: List<Note> = listOf(),
)
