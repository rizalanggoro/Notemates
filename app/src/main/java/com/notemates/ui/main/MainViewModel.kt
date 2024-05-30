package com.notemates.ui.main

import androidx.lifecycle.ViewModel
import com.notemates.data.models.User
import com.notemates.data.repositories.AuthRepository
import com.notemates.data.repositories.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val noteRepository: NoteRepository,
) : ViewModel() {
    val authenticatedUser: User? get() = authRepository.authenticatedUser
}