package com.notemates.ui.main.fragments.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.notemates.core.utils.StateStatus
import com.notemates.core.utils.Utils
import com.notemates.databinding.FragmentDashboardBinding
import com.notemates.ui.adapters.NoteAdapter
import com.notemates.ui.detail.note.DetailNoteActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        viewModel.getNotes()

        lifecycleScope.launch {
            viewModel.uiState.collect {
                val (status, message) = it
                if (status == StateStatus.Loading)
                    showLoadingUi(true)
                else {
                    showLoadingUi(false)
                    if (status == StateStatus.Failure)
                        Utils.showSnackbar(binding.root, message)
                    else if (status == StateStatus.Success)
                        noteAdapter.setNotes(it.response)
                }
            }
        }

        binding.apply {
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getNotes()
                if (swipeRefreshLayout.isRefreshing)
                    swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initRecyclerView() {
        noteAdapter = NoteAdapter(NoteAdapter.Type.Default, onItemClick = {
            startActivity(Intent(requireContext(), DetailNoteActivity::class.java).apply {
                putExtra("idNote", it)
            })
        })
        binding.recyclerView.apply {
            layoutManager = object : LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean = false
            }
            adapter = noteAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun showLoadingUi(isLoading: Boolean) {
        binding.apply {
            progressIndicator.isVisible = isLoading
        }
    }
}