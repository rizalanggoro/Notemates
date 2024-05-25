package com.notemates.ui.write.note

import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel

class CreateNoteViewModel : ViewModel() {
    val counter = ObservableInt(0)

    fun increment() = counter.set(counter.get() + 1)

}