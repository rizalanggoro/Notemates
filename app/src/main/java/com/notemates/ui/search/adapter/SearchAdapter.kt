package com.notemates.ui.search.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.notemates.R
import com.notemates.core.utils.dp
import com.notemates.data.models.responses.SearchResponse
import com.notemates.databinding.ListItemSearchNoteBinding
import com.notemates.databinding.ListItemSearchUserBinding


class SearchAdapter(
    private val context: Context,
    private val onItemClick: (item: Any) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var searchResponse: SearchResponse? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setData(searchResponse: SearchResponse) {
        this.searchResponse = searchResponse
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
        if (position < searchResponse!!.users.size) 0 else 1

    override fun getItemCount(): Int =
        if (searchResponse != null) searchResponse!!.users.size + searchResponse!!.notes.size
        else 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UserViewHolder -> {
                val user = searchResponse!!.users[position]
                with(holder) {
                    binding.apply {
                        textViewSectionName.isVisible = position == 0
                        textViewName.text = user.name
                        textViewEmail.text = user.email
                        textViewFollowedBy.text = String.format(
                            context.getString(R.string.format_followers),
                            user.count.followedBy
                        )

                        container.setOnClickListener {
                            onItemClick(user)
                        }
                    }
                }
            }

            is NoteViewHolder -> {
                val usersSize = searchResponse!!.users.size
                val note = searchResponse!!.notes[position - usersSize]
                with(holder) {
                    binding.apply {
                        val newPosition = (position - usersSize)

                        (cardView.layoutParams as LinearLayout.LayoutParams).apply {
                            setMargins(16.dp, if (newPosition == 0) 0 else 4.dp, 16.dp, 0)
                        }

                        dividerSection.isVisible = newPosition == 0
                        textViewSectionName.isVisible = newPosition == 0
                        textViewTitle.text = note.title
                        textViewDescription.text = note.description
                        textViewUser.text = note.user.name
                        textViewLikes.text = note.count.likes.toString()
                        textViewViews.text = note.views.toString()

                        cardView.setOnClickListener {
                            onItemClick(note)
                        }
                    }
                }
            }
        }
    }
}