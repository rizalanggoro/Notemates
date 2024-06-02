package com.notemates.ui.detail.user.follows.adapters

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.notemates.R
import com.notemates.ui.detail.user.follows.fragments.FollowsFragment

class FollowsFragmentStateAdapter(
    activity: AppCompatActivity,
    private val idUser: Int,
) : FragmentStateAdapter(activity) {
    companion object {
        @StringRes
        val titles = intArrayOf(
            R.string.follower,
            R.string.following,
        )
    }

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = FollowsFragment().apply {
        arguments = Bundle().apply {
            putInt("type", titles[position])
            putInt("idUser", idUser)
        }
    }
}