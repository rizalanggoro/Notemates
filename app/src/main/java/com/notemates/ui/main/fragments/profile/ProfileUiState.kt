package com.notemates.ui.main.fragments.profile

sealed class ProfileUiState {
    data object Initial : ProfileUiState()
    data object LogoutSuccess : ProfileUiState()
}