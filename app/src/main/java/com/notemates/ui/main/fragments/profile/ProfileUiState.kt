package com.notemates.ui.main.fragments.profile

import com.notemates.core.utils.StateStatus
import com.notemates.data.models.responses.UserProfileResponse

data class ProfileUiState(
    val action: Action = Action.Initial,
    val status: StateStatus = StateStatus.Initial,
    val message: String = "",
    val response: UserProfileResponse? = null,
) {
    enum class Action {
        Initial,
        LoadProfile,
        Logout,
        Delete,
    }
}