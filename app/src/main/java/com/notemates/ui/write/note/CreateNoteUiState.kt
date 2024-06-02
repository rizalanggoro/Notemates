package com.notemates.ui.write.note

import com.notemates.core.utils.StateStatus

data class CreateNoteUiState(
    val action: Action = Action.Initial,
    val status: StateStatus = StateStatus.Initial,
    val message: String = "",
    val content: String = "# Hello world!",
) {
    enum class Action {
        Initial,
        ChangeContent,
        Publish,
    }
}