package com.notemates.ui.write.note.adapters

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.notemates.R
import com.notemates.ui.write.note.fragments.CreateNoteContentFragment
import com.notemates.ui.write.note.fragments.CreateNoteDetailFragment
import com.notemates.ui.write.note.fragments.CreateNotePreviewFragment

class CreateNoteFragmentStateAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    companion object {
        @IdRes
        val titles = listOf(
            R.string.detail,
            R.string.content,
            R.string.preview,
        )
    }

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CreateNoteDetailFragment()
            1 -> CreateNoteContentFragment()
            else -> CreateNotePreviewFragment()
        }
    }
}