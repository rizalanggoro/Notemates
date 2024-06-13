package com.notemates.ui.main.fragments.profile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.notemates.R
import com.notemates.core.utils.StateStatus
import com.notemates.data.models.User
import com.notemates.data.repositories.AuthRepository
import com.notemates.data.repositories.NoteRepository
import com.notemates.data.repositories.UserRepository
import com.notemates.ui.main.fragments.profile.ProfileUiState.Action
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val application: Application,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val noteRepository: NoteRepository,
) : ViewModel() {
    val authenticatedUser: User? = authRepository.authenticatedUser

    private val _uiState: MutableStateFlow<ProfileUiState> =
        MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    fun loadProfile() {
        val idUser = authenticatedUser?.id
        if (idUser != null) {
            _uiState.value = uiState.value.copy(
                action = Action.LoadProfile,
                status = StateStatus.Loading,
            )
            viewModelScope.launch {
                val result = userRepository.getProfile(idUser, idUser)
                launch {
                    when (result) {
                        is Either.Left -> _uiState.value = uiState.value.copy(
                            action = Action.LoadProfile,
                            status = StateStatus.Failure,
                            message = result.value.message
                                ?: application.getString(R.string.something_went_wrong),
                        )

                        is Either.Right -> _uiState.value = uiState.value.copy(
                            action = Action.LoadProfile,
                            status = StateStatus.Success,
                            response = result.value,
                        )
                    }
                }
            }
        }
    }

    fun delete(
        idNote: Int
    ) {
        _uiState.value = uiState.value.copy(
            action = Action.Delete,
            status = StateStatus.Loading,
        )

        viewModelScope.launch {
            val result = noteRepository.delete(idNote)
            launch {
                when (result) {
                    is Either.Left -> _uiState.value = uiState.value.copy(
                        action = Action.Delete,
                        status = StateStatus.Failure,
                        message = result.value.message
                            ?: application.getString(R.string.something_went_wrong),
                    )

                    is Either.Right -> _uiState.value = uiState.value.copy(
                        action = Action.Delete,
                        status = StateStatus.Success,
                    )
                }
            }
        }
    }

    fun logout() {
        _uiState.value = uiState.value.copy(
            action = Action.Logout,
        )
        authRepository.logout()
    }
}