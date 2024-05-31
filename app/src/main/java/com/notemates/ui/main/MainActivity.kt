package com.notemates.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.notemates.R
import com.notemates.core.utils.StateStatus
import com.notemates.core.utils.Utils
import com.notemates.databinding.ActivityMainBinding
import com.notemates.ui.auth.AuthActivity
import com.notemates.ui.search.adapter.SearchAdapter
import com.notemates.ui.write.note.CreateNoteActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    private val titles = mapOf(
        R.id.dashboard_fragment to R.string.dashboard,
        R.id.explore_fragment to R.string.explore,
        R.id.trending_fragment to R.string.trending,
        R.id.profile_fragment to R.string.profile,
    )

    private lateinit var searchAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (viewModel.authenticatedUser == null) {
            startActivity(Intent(applicationContext, AuthActivity::class.java))
            finish()
        }

        initRecyclerViewSearch()

        lifecycleScope.launch {
            viewModel.uiState.collect {
                val (action, status, message) = it
                when (action) {
                    MainUiAction.Initial -> {}
                    MainUiAction.Search -> {
                        if (status == StateStatus.Loading)
                            binding.progressIndicatorSearch.isVisible = true
                        else {
                            binding.progressIndicatorSearch.isVisible = false
                            if (status == StateStatus.Success)
                                searchAdapter.setData(
                                    users = it.searchUsers,
                                    notes = it.searchNotes,
                                )
                            else if (status == StateStatus.Failure)
                                Utils.showSnackbar(binding.root, message)
                        }
                    }
                }
            }
        }

        binding.apply {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { v, actionId, event ->
                val keyword = v.text.toString()
                searchBar.setText(keyword)
                viewModel.search(keyword)

                false
            }
            searchView.editText.doAfterTextChanged {
                if (it.toString().isEmpty()) searchBar.setText("")
            }

            fabCreateNote.setOnClickListener {
                startActivity(
                    Intent(
                        applicationContext,
                        CreateNoteActivity::class.java
                    )
                )
            }

            onBackPressedDispatcher.addCallback {
                if (searchView.isShowing) searchView.hide()
            }

            val bottomNavigationView = bottomNavigationView
            val navController = findNavController(R.id.fragment_container_view)
            bottomNavigationView.setupWithNavController(navController)

            navController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.profile_fragment -> {
                        toolbar.isVisible = true
                        searchBar.isVisible = false

                        setSupportActionBar(toolbar)
                        toolbar.title = getString(titles[destination.id]!!)

                        fabCreateNote.hide()
                    }

                    else -> {
                        toolbar.isVisible = false
                        searchBar.isVisible = true

                        setSupportActionBar(searchBar)
                        searchBar.hint = getString(titles[destination.id]!!)

                        fabCreateNote.show()
                    }
                }
            }
        }


//        startActivity(Intent(this, TestActivity::class.java))
    }

    private fun initRecyclerViewSearch() {
        searchAdapter = SearchAdapter()
        binding.recyclerViewSearch.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = searchAdapter
        }
    }
}