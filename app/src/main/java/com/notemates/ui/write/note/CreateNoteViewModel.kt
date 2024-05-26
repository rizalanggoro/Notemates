package com.notemates.ui.write.note

import android.app.Application
import android.widget.Toast
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import arrow.core.Either
import com.notemates.data.models.User
import com.notemates.data.repositories.AuthRepository
import com.notemates.data.repositories.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.noties.markwon.Markwon
import io.noties.markwon.editor.MarkwonEditor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateNoteViewModel @Inject constructor(
    private val application: Application,
    private val authRepository: AuthRepository,
    private val noteRepository: NoteRepository,
) : ViewModel() {
    val authenticatedUser: User? get() = authRepository.authenticatedUser
    val isLoading = ObservableBoolean(false)
    val markdownContent = MutableLiveData("")

    val markwon: Markwon = Markwon.create(application.applicationContext)
    val markwonEditor: MarkwonEditor = MarkwonEditor.create(markwon)

    fun publish(
        title: String,
        description: String,
    ) {
        val userKey = authenticatedUser?.key
        if (userKey != null) {
            isLoading.set(true)
            CoroutineScope(Dispatchers.IO).launch {
                val result = noteRepository.publish(
                    userKey = userKey,
                    title = title,
                    description = description,
                    content = markdownContent.value ?: ""
                )
                launch(Dispatchers.Main) {
                    isLoading.set(false)
                    when (result) {
                        is Either.Left -> {
                            Toast.makeText(
                                application.applicationContext,
                                "error: ${result.value.message}",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                        is Either.Right -> {
                            Toast.makeText(
                                application.applicationContext,
                                "success",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                }
            }

        }
    }
}