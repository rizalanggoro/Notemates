package com.notemates.ui.main.fragments.trending

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
import com.notemates.databinding.FragmentTrendingBinding
import com.notemates.ui.adapters.NoteAdapter
import com.notemates.ui.detail.note.DetailNoteActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TrendingFragment : Fragment() {
    private var _binding: FragmentTrendingBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<TrendingViewModel>()
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrendingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        viewModel.getTrending()

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
                        noteAdapter.setNotes(it.notes)
                }
            }
        }

        binding.apply {
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getTrending()
                if (swipeRefreshLayout.isRefreshing)
                    swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun showLoadingUi(isLoading: Boolean) {
        binding.progressIndicator.isVisible = isLoading
    }

    private fun initRecyclerView() {
        noteAdapter = NoteAdapter(NoteAdapter.Type.Default) {
            startActivity(Intent(requireContext(), DetailNoteActivity::class.java).apply {
                putExtra("idNote", it)
            })
        }
        binding.recyclerView.apply {
            layoutManager = object : LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean = false
            }
            adapter = noteAdapter
            isNestedScrollingEnabled = false
        }
    }
}