package com.notemates.ui.auth

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.notemates.R
import com.notemates.core.utils.StateStatus
import com.notemates.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val application: Application,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<AuthUiState> = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    fun switchMode() {
        _uiState.value = uiState.value.copy(
            action = AuthUiAction.SwitchAuthMode,
            isLogin = !uiState.value.isLogin,
        )
    }

    fun login(email: String, password: String) {
        _uiState.value = uiState.value.copy(
            action = AuthUiAction.Login,
            status = StateStatus.Loading,
        )

        viewModelScope.launch {
            val result = authRepository.login(email, password)
            launch {
                when (result) {
                    is Either.Left -> _uiState.value = uiState.value.copy(
                        action = AuthUiAction.Login,
                        status = StateStatus.Failure,
                        message = result.value.message
                            ?: application.getString(R.string.something_went_wrong)
                    )

                    is Either.Right -> _uiState.value = uiState.value.copy(
                        action = AuthUiAction.Login,
                        status = StateStatus.Success,
                    )
                }
            }
        }
    }

    fun register(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        _uiState.value = uiState.value.copy(
            action = AuthUiAction.Register,
            status = StateStatus.Loading,
        )

        viewModelScope.launch {
            val result = authRepository.register(
                name,
                email,
                password,
                confirmPassword
            )
            launch {
                when (result) {
                    is Either.Left -> _uiState.value = uiState.value.copy(
                        action = AuthUiAction.Register,
                        status = StateStatus.Failure,
                        message = result.value.message
                            ?: application.getString(R.string.something_went_wrong),
                    )

                    is Either.Right -> _uiState.value = uiState.value.copy(
                        action = AuthUiAction.Register,
                        status = StateStatus.Success,
                    )
                }
            }
        }
    }
}