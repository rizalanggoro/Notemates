package com.notemates.ui.detail.note

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.notemates.R
import com.notemates.core.utils.StateStatus
import com.notemates.data.repositories.AuthRepository
import com.notemates.data.repositories.NoteRepository
import com.notemates.ui.detail.note.DetailNoteUiState.Action
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailNoteViewModel @Inject constructor(
    private val application: Application,
    private val authRepository: AuthRepository,
    private val noteRepository: NoteRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<DetailNoteUiState> =
        MutableStateFlow(DetailNoteUiState())
    val uiState: StateFlow<DetailNoteUiState> = _uiState

    fun likeDislike(idNote: Int) {
        val isLiked = uiState.value.currentIsLiked
        val idRequester = authRepository.idRequester
        if (idRequester != null) {
            _uiState.value = uiState.value.copy(
                action = Action.Like,
                status = StateStatus.Loading,
            )
            viewModelScope.launch {
                val result = if (isLiked) noteRepository.dislike(idRequester, idNote)
                else noteRepository.like(idRequester, idNote)
                launch {
                    when (result) {
                        is Either.Left -> _uiState.value = uiState.value.copy(
                            action = Action.Like,
                            status = StateStatus.Failure,
                            message = result.value.message
                                ?: application.getString(R.string.something_went_wrong),
                        )

                        is Either.Right -> _uiState.value = uiState.value.copy(
                            action = Action.Like,
                            status = StateStatus.Success,
                            currentIsLiked = !isLiked,
                            currentLikesCount = uiState.value.currentLikesCount + (if (isLiked) -1 else 1),
                        )
                    }
                }
            }

        }
    }

    fun getNoteById(idNote: Int) {
        val idRequester = authRepository.idRequester
        if (idRequester != null) {
            _uiState.value = uiState.value.copy(
                action = Action.GetNote,
                status = StateStatus.Loading,
            )

            viewModelScope.launch {
                noteRepository.incrementView(idNote)
                val result = noteRepository.get(idRequester, idNote)
                launch {
                    when (result) {
                        is Either.Left -> _uiState.value = uiState.value.copy(
                            action = Action.GetNote,
                            status = StateStatus.Failure,
                            message = result.value.message
                                ?: application.getString(R.string.something_went_wrong)
                        )

                        is Either.Right -> _uiState.value = uiState.value.copy(
                            action = Action.GetNote,
                            status = StateStatus.Success,
                            response = result.value,
                            currentIsLiked = result.value.isLiked,
                            currentLikesCount = result.value.count.likes,
                        )
                    }
                }
            }
        }
    }
}