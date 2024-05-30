package com.notemates.ui.auth

import com.notemates.core.utils.StateStatus

enum class AuthUiAction {
    Initial, Login, Register, SwitchAuthMode,
}

data class AuthUiState(
    val action: AuthUiAction = AuthUiAction.Initial,
    val status: StateStatus = StateStatus.Initial,
    val message: String = "",
    val isLogin: Boolean = true,
)