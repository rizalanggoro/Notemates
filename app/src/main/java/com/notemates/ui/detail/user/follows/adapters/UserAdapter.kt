package com.notemates.ui.detail.user.follows.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.notemates.data.models.User
import com.notemates.databinding.ListItemFollowsBinding

class UserAdapter(
    private val onItemClick: (idUser: Int) -> Unit,
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private var users: List<User> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setUsers(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ListItemFollowsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemFollowsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            binding.apply {
                val user = users[position]
                textViewName.text = user.name
                textViewEmail.text = user.email

                container.setOnClickListener {
                    onItemClick(user.id)
                }
            }
        }
    }
}