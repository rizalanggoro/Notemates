package com.notemates.ui.detail.user.follows

import com.notemates.core.utils.StateStatus
import com.notemates.data.models.User

data class FollowsUiState(
    val action: Action = Action.Initial,
    val status: StateStatus = StateStatus.Initial,
    val message: String = "",
    val followedBy: List<User> = listOf(),
    val following: List<User> = listOf(),
) {
    enum class Action {
        Initial,
        GetFollowedBy,
        GetFollowing,
    }
}
