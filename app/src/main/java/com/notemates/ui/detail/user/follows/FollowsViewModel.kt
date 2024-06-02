package com.notemates.ui.detail.user.follows

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.notemates.R
import com.notemates.core.utils.StateStatus
import com.notemates.data.repositories.UserRepository
import com.notemates.ui.detail.user.follows.FollowsUiState.Action
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowsViewModel @Inject constructor(
    private val application: Application,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<FollowsUiState> = MutableStateFlow(FollowsUiState())
    val uiState: StateFlow<FollowsUiState> = _uiState

    fun getFollowedBy(idUser: Int) {
        _uiState.value = uiState.value.copy(
            action = Action.GetFollowedBy,
            status = StateStatus.Loading,
        )
        viewModelScope.launch {
            val result = userRepository.getFollowedBy(idUser)
            launch {
                when (result) {
                    is Either.Left -> _uiState.value = uiState.value.copy(
                        action = Action.GetFollowedBy,
                        status = StateStatus.Failure,
                        message = result.value.message
                            ?: application.getString(R.string.something_went_wrong),
                    )

                    is Either.Right -> _uiState.value = uiState.value.copy(
                        action = Action.GetFollowedBy,
                        status = StateStatus.Success,
                        followedBy = result.value,
                    )
                }
            }
        }
    }

    fun getFollowing(idUser: Int) {
        _uiState.value = uiState.value.copy(
            action = Action.GetFollowing,
            status = StateStatus.Loading,
        )
        viewModelScope.launch {
            val result = userRepository.getFollowing(idUser)
            launch {
                when (result) {
                    is Either.Left -> _uiState.value = uiState.value.copy(
                        action = Action.GetFollowing,
                        status = StateStatus.Failure,
                        message = result.value.message
                            ?: application.getString(R.string.something_went_wrong),
                    )

                    is Either.Right -> _uiState.value = uiState.value.copy(
                        action = Action.GetFollowing,
                        status = StateStatus.Success,
                        following = result.value,
                    )
                }
            }
        }
    }
}