package com.notemates.ui.detail.note

import com.notemates.core.utils.StateStatus
import com.notemates.data.models.Note

data class DetailNoteUiState(
    val action: Action = Action.Initial,
    val status: StateStatus = StateStatus.Initial,
    val message: String = "",
    val response: Note? = null,
    val currentIsLiked: Boolean = false,
    val currentLikesCount: Int = 0,
) {
    enum class Action {
        Initial,
        Like,
        GetNote,
    }
}
