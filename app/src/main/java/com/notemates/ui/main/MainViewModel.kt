package com.notemates.ui.main

import androidx.lifecycle.ViewModel
import com.notemates.data.models.User
import com.notemates.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    val authenticatedUser: User? get() = authRepository.authenticatedUser
}