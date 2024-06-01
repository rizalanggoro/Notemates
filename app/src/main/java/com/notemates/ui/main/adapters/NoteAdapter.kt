package com.notemates.ui.main.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.notemates.core.utils.dp
import com.notemates.data.models.Note
import com.notemates.databinding.ListItemNoteBinding

class NoteAdapter(
    private val onItemClick: (position: Int, note: Note) -> Unit,
) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    private var listNotes: List<Note> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(listNotes: List<Note>) {
        this.listNotes = listNotes
        notifyDataSetChanged()
    }

    class ViewHolder(
        val binding: ListItemNoteBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemNoteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun getItemCount(): Int = listNotes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val note = listNotes[position]

            val layoutParams = binding.cardView.layoutParams as LinearLayout.LayoutParams
            layoutParams.setMargins(
                16.dp,
                if (position == 0) 0 else 8.dp,
                16.dp, 0
            )
            binding.cardView.layoutParams = layoutParams

            binding.cardView.setOnClickListener {
                onItemClick(position, note)
            }
            binding.textViewTitle.text = note.title
            binding.textViewDescription.text = note.description
//            binding.textViewView.text = note.view.toString()
//            binding.textViewLike.text = note.like.toString()
        }
    }
}