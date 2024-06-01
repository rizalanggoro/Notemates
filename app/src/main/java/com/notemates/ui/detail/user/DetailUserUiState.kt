package com.notemates.ui.detail.user

import com.notemates.core.utils.StateStatus
import com.notemates.data.models.responses.UserProfileResponse

data class DetailUserUiState(
    val status: StateStatus = StateStatus.Initial,
    val message: String = "",
    val response: UserProfileResponse? = null,
)
