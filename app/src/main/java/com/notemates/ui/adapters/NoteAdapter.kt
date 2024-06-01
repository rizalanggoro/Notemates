package com.notemates.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.notemates.core.utils.dp
import com.notemates.data.models.responses.UserProfileResponse
import com.notemates.databinding.ListItemNoteBinding
import com.notemates.databinding.ListItemProfileNoteBinding

class NoteAdapter<T>(
    private val type: Type,
) : RecyclerView.Adapter<ViewHolder>() {
    private var data: List<T> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<T>) {
        this.data = data
        notifyDataSetChanged()
    }

    enum class Type {
        Main,
        Profile,
    }

    class ProfileViewHolder(val binding: ListItemProfileNoteBinding) : ViewHolder(binding.root)
    class MainViewHolder(val binding: ListItemNoteBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = when (type) {
        Type.Main -> MainViewHolder(
            ListItemNoteBinding.inflate(
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

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            when (this) {
                is MainViewHolder -> {}
                is ProfileViewHolder -> {
                    val item = data[position] as UserProfileResponse.Note

                    binding.apply {
                        textViewTitle.text = item.title
                        textViewDescription.text = item.description
                        textViewComments.text = item.count.comments.toString()
                        textViewViews.text = item.views.toString()
                        textViewLikes.text = item.count.likes.toString()

                        (cardView.layoutParams as LinearLayout.LayoutParams).apply {
                            setMargins(16.dp, if (position == 0) 0 else 4.dp, 16.dp, 0)
                        }

                        cardView.setOnClickListener { }
                    }
                }
            }
        }
    }
}