package com.notemates.ui.main.fragments.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.notemates.databinding.FragmentDashboardBinding
import com.notemates.ui.DetailNoteActivity
import com.notemates.ui.main.adapters.NoteAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DashboardViewModel>()
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        noteAdapter = NoteAdapter { position, note ->
            startActivity(
                Intent(requireContext(), DetailNoteActivity::class.java)
            )
        }

        binding.recyclerViewLatestNotes.apply {
            layoutManager = object : LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean = false
            }
            adapter = noteAdapter
        }

        viewModel.listNotes.observe(requireActivity()) {
            noteAdapter.setData(it)
        }

        binding.buttonRefreshLatestNotes.setOnClickListener { viewModel.refreshLatestNotes() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}