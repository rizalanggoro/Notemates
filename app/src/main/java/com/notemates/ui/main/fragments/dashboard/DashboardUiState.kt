package com.notemates.ui.main.fragments.dashboard

import com.notemates.core.utils.StateStatus
import com.notemates.data.models.Note

data class DashboardUiState(
    val status: StateStatus = StateStatus.Initial,
    val message: String = "",
    val response: List<Note> = listOf(),
)
