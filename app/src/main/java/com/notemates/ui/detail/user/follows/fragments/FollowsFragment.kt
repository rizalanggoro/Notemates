package com.notemates.ui.detail.user.follows.fragments

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
import com.notemates.R
import com.notemates.core.utils.StateStatus
import com.notemates.core.utils.Utils
import com.notemates.databinding.FragmentFollowsBinding
import com.notemates.ui.detail.user.DetailUserActivity
import com.notemates.ui.detail.user.follows.FollowsUiState
import com.notemates.ui.detail.user.follows.FollowsViewModel
import com.notemates.ui.detail.user.follows.adapters.UserAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FollowsFragment : Fragment() {
    private var _binding: FragmentFollowsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<FollowsViewModel>()
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idUser = arguments?.getInt("idUser")
        if (idUser == null) requireActivity().finish()

        initRecyclerView()

        when (arguments?.getInt("type")) {
            R.string.follower -> viewModel.getFollowedBy(idUser!!)
            R.string.following -> viewModel.getFollowing(idUser!!)
        }

        lifecycleScope.launch {
            viewModel.uiState.collect {
                val (action, status, message) = it
                when (action) {
                    FollowsUiState.Action.Initial -> {}
                    FollowsUiState.Action.GetFollowedBy -> {
                        if (status == StateStatus.Loading)
                            showLoadingUi(true)
                        else {
                            showLoadingUi(false)
                            if (status == StateStatus.Failure)
                                Utils.showSnackbar(binding.root, message)
                            else if (status == StateStatus.Success)
                                userAdapter.setUsers(it.followedBy)
                        }
                    }

                    FollowsUiState.Action.GetFollowing -> {
                        if (status == StateStatus.Loading)
                            showLoadingUi(true)
                        else {
                            showLoadingUi(false)
                            if (status == StateStatus.Failure)
                                Utils.showSnackbar(binding.root, message)
                            else if (status == StateStatus.Success)
                                userAdapter.setUsers(it.following)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initRecyclerView() {
        userAdapter = UserAdapter {
            startActivity(Intent(requireContext(), DetailUserActivity::class.java).apply {
                putExtra("idUser", it)
            })
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userAdapter
        }
    }

    private fun showLoadingUi(isLoading: Boolean) {
        binding.progressIndicator.isVisible = isLoading
    }
}