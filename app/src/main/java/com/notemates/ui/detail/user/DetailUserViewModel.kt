package com.notemates.ui.detail.user

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.notemates.R
import com.notemates.core.utils.StateStatus
import com.notemates.data.repositories.AuthRepository
import com.notemates.data.repositories.UserRepository
import com.notemates.ui.detail.user.DetailUserUiState.Action
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailUserViewModel @Inject constructor(
    private val application: Application,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<DetailUserUiState> =
        MutableStateFlow(DetailUserUiState())
    val uiState: StateFlow<DetailUserUiState> = _uiState

    fun followUnfollow(
        idUser: Int,
    ) {
        val idRequester = authRepository.idRequester
        if (idRequester != null) {
            val isFollowed = uiState.value.currentIsFollowed
            _uiState.value = uiState.value.copy(
                action = Action.FollowUnfollow,
                status = StateStatus.Loading,
            )
            viewModelScope.launch {
                val result = if (isFollowed) userRepository.unfollow(idRequester, idUser)
                else userRepository.follow(idRequester, idUser)
                launch {
                    when (result) {
                        is Either.Left -> _uiState.value = uiState.value.copy(
                            action = Action.FollowUnfollow,
                            status = StateStatus.Failure,
                            message = result.value.message
                                ?: application.getString(R.string.something_went_wrong)
                        )

                        is Either.Right -> _uiState.value = uiState.value.copy(
                            action = Action.FollowUnfollow,
                            status = StateStatus.Success,
                            currentIsFollowed = !isFollowed,
                            currentFollowedBy = uiState.value.currentFollowedBy + (if (isFollowed) -1 else 1),
                        )
                    }
                }
            }
        }
    }

    fun getProfile(
        idUser: Int,
    ) {
        val idRequester = authRepository.authenticatedUser?.id
        if (idRequester != null) {
            _uiState.value = uiState.value.copy(
                action = Action.GetProfile,
                status = StateStatus.Loading,
            )

            viewModelScope.launch {
                val result = userRepository.getProfile(
                    idUser = idUser,
                    idRequester = idRequester,
                )
                launch {
                    when (result) {
                        is Either.Left -> _uiState.value = uiState.value.copy(
                            action = Action.GetProfile,
                            status = StateStatus.Failure,
                            message = result.value.message
                                ?: application.getString(R.string.something_went_wrong),
                        )

                        is Either.Right -> _uiState.value = uiState.value.copy(
                            action = Action.GetProfile,
                            status = StateStatus.Success,
                            response = result.value,
                            currentIsFollowed = result.value.isFollowed,
                            currentFollowedBy = result.value.count.followedBy,
                        )
                    }
                }
            }
        }
    }
}