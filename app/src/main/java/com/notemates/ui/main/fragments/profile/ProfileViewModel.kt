package com.notemates.ui.main.fragments.profile

import androidx.lifecycle.ViewModel
import com.notemates.data.models.User
import com.notemates.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    val authenticatedUser: User? = authRepository.authenticatedUser

    private val _uiState: MutableStateFlow<ProfileUiState> =
        MutableStateFlow(ProfileUiState.Initial)
    val uiState: StateFlow<ProfileUiState> = _uiState

    fun logout() {
        authRepository.logout()
        _uiState.value = ProfileUiState.LogoutSuccess
    }
}