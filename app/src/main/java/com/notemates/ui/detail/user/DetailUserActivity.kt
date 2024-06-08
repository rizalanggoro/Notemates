package com.notemates.ui.detail.user

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
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
import com.notemates.data.models.responses.UserProfileResponse
import com.notemates.databinding.ActivityDetailUserBinding
import com.notemates.ui.adapters.NoteAdapter
import com.notemates.ui.detail.note.DetailNoteActivity
import com.notemates.ui.detail.user.follows.FollowsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private val viewModel by viewModels<DetailUserViewModel>()
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val idUser = intent.extras?.getInt("idUser", -1)
        if (idUser == null || idUser == -1) finish()

        initRecyclerView()
        viewModel.getProfile(idUser!!)

        lifecycleScope.launch {
            viewModel.uiState.collect {
                val (action, status, message) = it
                when (action) {
                    DetailUserUiState.Action.Initial -> {}
                    DetailUserUiState.Action.GetProfile -> {
                        if (status == StateStatus.Loading)
                            showLoadingUi(true)
                        else {
                            showLoadingUi(false)
                            if (status == StateStatus.Failure)
                                Utils.showSnackbar(binding.root, message)
                            else if (status == StateStatus.Success) {
                                val response = it.response
                                if (response != null) showProfileUi(response)
                            }
                        }
                    }

                    DetailUserUiState.Action.FollowUnfollow -> {
                        if (status == StateStatus.Failure)
                            Utils.showSnackbar(binding.root, message)
                        else if (status == StateStatus.Success) {
                            binding.buttonFollow.isVisible = !it.currentIsFollowed
                            binding.buttonUnfollow.isVisible = it.currentIsFollowed
                            binding.textViewFollowedByCount.text = it.currentFollowedBy.toString()
                        }
                    }
                }
            }
        }

        binding.apply {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getProfile(idUser)
                if (swipeRefreshLayout.isRefreshing)
                    swipeRefreshLayout.isRefreshing = false
            }

            val gotoFollows = fun() {
                startActivity(Intent(applicationContext, FollowsActivity::class.java).apply {
                    putExtra("idUser", idUser)
                })
            }
            linearLayoutFollowedBy.setOnClickListener { gotoFollows() }
            linearLayoutFollowing.setOnClickListener { gotoFollows() }

            buttonFollow.setOnClickListener { viewModel.followUnfollow(idUser) }
            buttonUnfollow.setOnClickListener { viewModel.followUnfollow(idUser) }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRecyclerView() {
        noteAdapter = NoteAdapter(type = NoteAdapter.Type.Profile) {
            startActivity(Intent(applicationContext, DetailNoteActivity::class.java).apply {
                putExtra("idNote", it)
            })
        }

        binding.recyclerViewNotes.apply {
            layoutManager = object : LinearLayoutManager(application) {
                override fun canScrollVertically(): Boolean = false
            }
            adapter = noteAdapter
        }
    }

    private fun showProfileUi(response: UserProfileResponse) {
        binding.apply {
            content.isVisible = true
            textViewName.text = response.name
            textViewEmail.text = response.email
            textViewFollowedByCount.text = response.count.followedBy.toString()
            textViewFollowingCount.text = response.count.following.toString()
            textViewNotesCount.text = response.count.notes.toString()

            buttonFollow.isVisible = !response.isFollowed
            buttonUnfollow.isVisible = response.isFollowed
        }

        noteAdapter.setNotes(response.notes)
    }

    private fun showLoadingUi(isLoading: Boolean) {
        binding.apply {
            progressIndicator.isVisible = isLoading
            content.isVisible = !isLoading
        }
    }
}