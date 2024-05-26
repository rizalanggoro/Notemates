package com.notemates.ui.main.fragments.profile

import androidx.lifecycle.ViewModel
import com.notemates.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    val authenticatedUser = authRepository.authenticatedUser

    fun logout() = authRepository.logout()

}