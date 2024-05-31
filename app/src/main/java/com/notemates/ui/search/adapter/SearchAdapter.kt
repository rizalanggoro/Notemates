package com.notemates.ui.search.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.notemates.data.models.Note
import com.notemates.data.models.User
import com.notemates.databinding.ListItemSearchNoteBinding
import com.notemates.databinding.ListItemSearchUserBinding

class SearchAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var users: List<User> = listOf()
    private var notes: List<Note> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(users: List<User>, notes: List<Note>) {
        this.users = users
        this.notes = notes
        notifyDataSetChanged()
    }

    class UserViewHolder(val binding: ListItemSearchUserBinding) :
        RecyclerView.ViewHolder(binding.root)

    class NoteViewHolder(val binding: ListItemSearchNoteBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            0 -> UserViewHolder(
                ListItemSearchUserBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> NoteViewHolder(
                ListItemSearchNoteBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }


    override fun getItemViewType(position: Int): Int =
        if (position < users.size) 0 else 1

    override fun getItemCount(): Int = users.size + notes.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UserViewHolder -> {
                val user = users[position]
                with(holder) {
                    binding.apply {
                        textViewSectionName.isVisible = position == 0
                        textViewName.text = user.name
                        textViewEmail.text = user.email
                    }
                }
            }

            is NoteViewHolder -> {
                val note = notes[position - users.size]
                with(holder) {
                    binding.apply {
                        textViewSectionName.isVisible = (position - users.size) == 0
                        textViewTitle.text = note.title
                        textViewDescription.text = note.description
                    }
                }
            }
        }
    }
}