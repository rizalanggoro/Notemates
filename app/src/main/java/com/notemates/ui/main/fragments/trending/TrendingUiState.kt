package com.notemates.ui.main.fragments.trending

import com.notemates.core.utils.StateStatus
import com.notemates.data.models.Note

data class TrendingUiState(
    val status: StateStatus = StateStatus.Initial,
    val message: String = "",
    val notes: List<Note> = listOf(),
)