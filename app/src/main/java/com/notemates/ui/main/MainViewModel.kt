package com.notemates.ui.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.notemates.R
import com.notemates.core.utils.StateStatus
import com.notemates.data.models.User
import com.notemates.data.repositories.AuthRepository
import com.notemates.data.repositories.NoteRepository
import com.notemates.data.repositories.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val application: Application,
    private val authRepository: AuthRepository,
    private val noteRepository: NoteRepository,
    private val searchRepository: SearchRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    val authenticatedUser: User? get() = authRepository.authenticatedUser

    fun search(keyword: String) {
        _uiState.value = uiState.value.copy(
            action = MainUiAction.Search,
            status = StateStatus.Loading,
        )

        viewModelScope.launch {
            val result = searchRepository.search(keyword)
            launch {
                when (result) {
                    is Either.Left -> _uiState.value = uiState.value.copy(
                        action = MainUiAction.Search,
                        status = StateStatus.Failure,
                        message = result.value.message
                            ?: application.getString(R.string.something_went_wrong),
                    )

                    is Either.Right -> _uiState.value = uiState.value.copy(
                        action = MainUiAction.Search,
                        status = StateStatus.Success,
                        searchUsers = result.value.users,
                        searchNotes = result.value.notes,
                    )
                }
            }
        }
    }
}