package com.notemates.ui.search

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.notemates.core.utils.StateStatus
import com.notemates.core.utils.Utils
import com.notemates.databinding.ActivitySearchBinding
import com.notemates.ui.search.adapter.SearchAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val viewModel by viewModels<SearchViewModel>()
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initializeRecyclerView()

        lifecycleScope.launch {
            viewModel.uiState.collect {
                val (action, status, message) = it
                when (action) {
                    SearchUiAction.Initial -> {}
                    SearchUiAction.Search -> {
                        if (status == StateStatus.Loading)
                            showLoadingUi(true)
                        else {
                            showLoadingUi(false)
                            if (status == StateStatus.Success)
                                searchAdapter.setData(
                                    users = it.users,
                                    notes = it.notes,
                                )
                            else if (status == StateStatus.Failure)
                                Utils.showSnackbar(binding.root, message)
                        }
                    }
                }
            }
        }

        binding.apply {
            buttonSearch.setOnClickListener {
                val keyword = editTextSearch.text.toString()
                viewModel.search(keyword)
            }
        }
    }

    private fun initializeRecyclerView() {
        searchAdapter = SearchAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = searchAdapter
        }
    }

    private fun showLoadingUi(isLoading: Boolean) {
        binding.apply {
            progressIndicator.isVisible = isLoading
            buttonSearch.isEnabled = !isLoading
            editTextSearch.isEnabled = !isLoading
        }
    }
}