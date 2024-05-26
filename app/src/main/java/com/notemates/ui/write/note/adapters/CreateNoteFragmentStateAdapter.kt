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
            R.string.content,
            R.string.preview,
            R.string.detail,
        )
    }

    override fun getItemCount(): Int = titles.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CreateNoteContentFragment()
            1 -> CreateNotePreviewFragment()
            else -> CreateNoteDetailFragment()
        }
    }
}