package com.notemates.ui.main.fragments.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.notemates.core.utils.StateStatus
import com.notemates.core.utils.Utils
import com.notemates.data.models.responses.UserProfileResponse
import com.notemates.databinding.FragmentProfileBinding
import com.notemates.ui.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadProfile()

        lifecycleScope.launch {
            viewModel.uiState.collect {
                val (action, status, message) = it
                when (action) {
                    ProfileUiState.Action.Initial -> {}
                    ProfileUiState.Action.LoadProfile -> {
                        binding.progressIndicator.isVisible = status == StateStatus.Loading
                        if (status == StateStatus.Failure)
                            Utils.showSnackbar(binding.root, message)
                        else if (status == StateStatus.Success)
                            if (it.response != null)
                                showProfileUi(it.response)
                    }

                    ProfileUiState.Action.Logout -> startActivity(
                        Intent(
                            requireContext(),
                            AuthActivity::class.java
                        )
                    ).also { requireActivity().finish() }
                }
            }
        }

        binding.apply {
            textViewName.text = viewModel.authenticatedUser?.name ?: "No name"
            textViewEmail.text = viewModel.authenticatedUser?.email ?: "No email"

            buttonLogout.setOnClickListener {
                viewModel.logout()
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.loadProfile()
                if (swipeRefreshLayout.isRefreshing)
                    swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showProfileUi(response: UserProfileResponse) {
        binding.apply {
            textViewName.text = response.name
            textViewEmail.text = response.email
            textViewFollowedByCount.text = response.count.followedBy.toString()
            textViewFollowingCount.text = response.count.following.toString()
            textViewNotesCount.text = response.count.notes.toString()
        }
    }
}