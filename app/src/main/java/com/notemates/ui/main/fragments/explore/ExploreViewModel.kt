package com.notemates.ui.main.fragments.explore

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.notemates.R
import com.notemates.core.utils.StateStatus
import com.notemates.data.repositories.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val application: Application,
    private val noteRepository: NoteRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<ExploreUiState> = MutableStateFlow(ExploreUiState())
    val uiState: StateFlow<ExploreUiState> = _uiState

    fun getLatest() {
        _uiState.value = uiState.value.copy(
            status = StateStatus.Loading,
        )
        viewModelScope.launch {
            val result = noteRepository.getLatest()
            launch {
                when (result) {
                    is Either.Left -> _uiState.value = uiState.value.copy(
                        status = StateStatus.Failure,
                        message = result.value.message
                            ?: application.getString(R.string.something_went_wrong)
                    )

                    is Either.Right -> _uiState.value = uiState.value.copy(
                        status = StateStatus.Success,
                        notes = result.value,
                    )
                }
            }
        }
    }
}