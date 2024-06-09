package com.notemates.ui.main.fragments.explore

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.notemates.R
import com.notemates.core.utils.StateStatus
import com.notemates.core.utils.Utils
import com.notemates.databinding.FragmentExploreBinding
import com.notemates.ui.adapters.NoteAdapter
import com.notemates.ui.detail.note.DetailNoteActivity
import com.notemates.ui.search.SearchActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExploreFragment : Fragment() {
    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ExploreViewModel>()

    private lateinit var noteAdapterPopular: NoteAdapter
    private lateinit var noteAdapterLatest: NoteAdapter
    private lateinit var menuProvider: MenuProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().removeMenuProvider(menuProvider)
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initToolbarMenu()

        viewModel.getPopular()
        viewModel.getLatest()

        lifecycleScope.launch {
            viewModel.uiState.collect {
                val (action, status, message) = it
                when (action) {
                    ExploreUiState.Action.Initial -> {}
                    ExploreUiState.Action.GetPopular -> {
                        if (status == StateStatus.Loading)
                            showLoadingUi(true)
                        else {
                            showLoadingUi(false)
                            if (status == StateStatus.Failure)
                                Utils.showSnackbar(binding.root, message)
                            else if (status == StateStatus.Success)
                                noteAdapterPopular.setNotes(it.popularNotes)
                        }
                    }

                    ExploreUiState.Action.GetLatest -> {
                        if (status == StateStatus.Loading)
                            showLoadingUi(true)
                        else {
                            showLoadingUi(false)
                            if (status == StateStatus.Failure)
                                Utils.showSnackbar(binding.root, message)
                            else if (status == StateStatus.Success)
                                noteAdapterLatest.setNotes(it.latestNotes)
                        }
                    }
                }
            }
        }

        binding.apply {
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getPopular()
                viewModel.getLatest()
                if (swipeRefreshLayout.isRefreshing)
                    swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun showLoadingUi(isLoading: Boolean) {
        binding.progressIndicator.isVisible = isLoading
    }

    private fun initToolbarMenu() {
        menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_explore, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menuSearch -> startActivity(
                        Intent(
                            requireContext(),
                            SearchActivity::class.java
                        )
                    )
                }
                return true
            }
        }

        requireActivity().addMenuProvider(menuProvider)
    }

    private fun initRecyclerView() {
        noteAdapterPopular = NoteAdapter(NoteAdapter.Type.Default, onItemClick = {
            startActivity(Intent(requireContext(), DetailNoteActivity::class.java).apply {
                putExtra("idNote", it)
            })
        })

        noteAdapterLatest = NoteAdapter(NoteAdapter.Type.Default, onItemClick = {
            startActivity(Intent(requireContext(), DetailNoteActivity::class.java).apply {
                putExtra("idNote", it)
            })
        })

        binding.recyclerViewPopular.apply {
            layoutManager = object : LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean = false
            }
            adapter = noteAdapterPopular
            isNestedScrollingEnabled = false
        }

        binding.recyclerViewLatest.apply {
            layoutManager = object : LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean = false
            }
            adapter = noteAdapterLatest
            isNestedScrollingEnabled = false
        }
    }
}