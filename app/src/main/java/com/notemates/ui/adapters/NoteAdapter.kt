package com.notemates.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.notemates.core.utils.dp
import com.notemates.data.models.Note
import com.notemates.databinding.ListItemDefaultNoteBinding
import com.notemates.databinding.ListItemProfileNoteBinding

class NoteAdapter(
    private val type: Type,
    private val onItemClick: (idNote: Int) -> Unit,
) : RecyclerView.Adapter<ViewHolder>() {
    private var notes: List<Note> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setNotes(notes: List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }

    enum class Type {
        Default,
        Profile,
    }

    class ProfileViewHolder(val binding: ListItemProfileNoteBinding) : ViewHolder(binding.root)
    class DefaultViewHolder(val binding: ListItemDefaultNoteBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = when (type) {
        Type.Default -> DefaultViewHolder(
            ListItemDefaultNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        Type.Profile -> ProfileViewHolder(
            ListItemProfileNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = notes[position]
        holder.apply {
            when (this) {
                is DefaultViewHolder -> {
                    binding.apply {
                        textViewTitle.text = item.title
                        textViewDescription.text = item.description
                        textViewUser.text = item.user.name
                        textViewLikes.text = item.count.likes.toString()
                        textViewViews.text = item.views.toString()

                        (cardView.layoutParams as LinearLayout.LayoutParams).apply {
                            setMargins(16.dp, if (position == 0) 0 else 4.dp, 16.dp, 0)
                        }
                        cardView.setOnClickListener {
                            onItemClick(item.id)
                        }
                    }
                }

                is ProfileViewHolder -> {
                    binding.apply {
                        textViewTitle.text = item.title
                        textViewDescription.text = item.description
                        textViewViews.text = item.views.toString()
                        textViewLikes.text = item.count.likes.toString()

                        (cardView.layoutParams as LinearLayout.LayoutParams).apply {
                            setMargins(16.dp, if (position == 0) 0 else 4.dp, 16.dp, 0)
                        }
                        cardView.setOnClickListener {
                            onItemClick(item.id)
                        }
                    }
                }
            }
        }
    }
}