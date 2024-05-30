package com.notemates.ui.main.fragments.dashboard

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import arrow.core.Either
import com.notemates.data.models.Note
import com.notemates.data.models.User
import com.notemates.data.repositories.AuthRepository
import com.notemates.data.repositories.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val noteRepository: NoteRepository,
) : ViewModel() {
    val authenticatedUser: User? get() = authRepository.authenticatedUser

    val isLoading = ObservableBoolean(false)
    val listNotes = MutableLiveData<List<Note>>(listOf())

    init {
        getLatestNotes()
    }

    private fun getLatestNotes() {
        
    }

    fun refreshLatestNotes() = getLatestNotes()
}