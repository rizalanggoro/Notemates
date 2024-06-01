package com.notemates.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
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
import com.notemates.data.models.Note
import com.notemates.data.models.responses.SearchResponse
import com.notemates.databinding.ActivitySearchBinding
import com.notemates.ui.detail.note.DetailNoteActivity
import com.notemates.ui.detail.user.DetailUserActivity
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
                    SearchUiState.Action.Initial -> {}
                    SearchUiState.Action.Search -> {
                        if (status == StateStatus.Loading)
                            showLoadingUi(true)
                        else {
                            showLoadingUi(false)
                            if (status == StateStatus.Success) {
                                if (it.searchResponse != null)
                                    searchAdapter.setData(it.searchResponse)
                            } else if (status == StateStatus.Failure)
                                Utils.showSnackbar(binding.root, message)
                        }
                    }
                }
            }
        }

        binding.apply {
            editTextSearch.setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.search(v.text.toString())
                    return@setOnEditorActionListener true
                }

                false
            }
        }
    }

    private fun initializeRecyclerView() {
        searchAdapter = SearchAdapter(applicationContext) {
            if (it is SearchResponse.User) startActivity(
                Intent(
                    applicationContext,
                    DetailUserActivity::class.java
                ).apply {
                    putExtra("idUser", it.id)
                })
            else if (it is Note) startActivity(
                Intent(
                    applicationContext,
                    DetailNoteActivity::class.java,
                ).apply {
                    putExtra("idNote", it.id)
                }
            )
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = searchAdapter
        }
    }

    private fun showLoadingUi(isLoading: Boolean) {
        binding.apply {
            progressIndicator.isVisible = isLoading
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}