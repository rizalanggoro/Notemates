package com.notemates.ui.detail.user

import com.notemates.core.utils.StateStatus
import com.notemates.data.models.responses.UserProfileResponse

data class DetailUserUiState(
    val action: Action = Action.Initial,
    val status: StateStatus = StateStatus.Initial,
    val message: String = "",
    val response: UserProfileResponse? = null,
    val currentIsFollowed: Boolean = false,
    val currentFollowedBy: Int = 0,
) {
    enum class Action {
        Initial,
        GetProfile,
        FollowUnfollow,
    }
}
