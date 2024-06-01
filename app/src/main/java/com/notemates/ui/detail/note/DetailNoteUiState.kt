package com.notemates.ui.detail.note

import com.notemates.core.utils.StateStatus
import com.notemates.data.models.Note

data class DetailNoteUiState(
    val status: StateStatus = StateStatus.Initial,
    val message: String = "",
    val response: Note? = null,
)
