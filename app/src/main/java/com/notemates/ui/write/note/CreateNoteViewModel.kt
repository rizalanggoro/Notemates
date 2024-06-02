package com.notemates.ui.write.note

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.notemates.R
import com.notemates.core.utils.StateStatus
import com.notemates.data.models.User
import com.notemates.data.repositories.AuthRepository
import com.notemates.data.repositories.NoteRepository
import com.notemates.ui.write.note.CreateNoteUiState.Action
import dagger.hilt.android.lifecycle.HiltViewModel
import io.noties.markwon.Markwon
import io.noties.markwon.editor.MarkwonEditor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateNoteViewModel @Inject constructor(
    private val application: Application,
    private val authRepository: AuthRepository,
    private val noteRepository: NoteRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<CreateNoteUiState> =
        MutableStateFlow(CreateNoteUiState())
    val uiState: StateFlow<CreateNoteUiState> = _uiState
    val authenticatedUser: User? get() = authRepository.authenticatedUser

    val markwon: Markwon = Markwon.create(application.applicationContext)
    val markwonEditor: MarkwonEditor = MarkwonEditor.create(markwon)

    fun changeContent(content: String) {
        _uiState.value = uiState.value.copy(
            action = Action.ChangeContent,
            content = content,
        )
    }

    fun publish(
        title: String,
        description: String,
    ) {
        val idUser = authenticatedUser?.id
        if (idUser != null) {
            _uiState.value = uiState.value.copy(
                action = Action.Publish,
                status = StateStatus.Loading,
            )
            viewModelScope.launch {
                val result = noteRepository.create(
                    title, description, uiState.value.content, idUser,
                )
                launch {
                    when (result) {
                        is Either.Left -> _uiState.value = uiState.value.copy(
                            action = Action.Publish,
                            status = StateStatus.Failure,
                            message = result.value.message
                                ?: application.getString(R.string.something_went_wrong),
                        )

                        is Either.Right -> _uiState.value = uiState.value.copy(
                            action = Action.Publish,
                            status = StateStatus.Success,
                        )
                    }
                }
            }
        }
    }
}