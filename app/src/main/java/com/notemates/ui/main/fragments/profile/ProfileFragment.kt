package com.notemates.ui.main.fragments.profile

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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.notemates.R
import com.notemates.core.utils.StateStatus
import com.notemates.core.utils.Utils
import com.notemates.data.models.responses.UserProfileResponse
import com.notemates.databinding.FragmentProfileBinding
import com.notemates.ui.adapters.NoteAdapter
import com.notemates.ui.auth.AuthActivity
import com.notemates.ui.detail.note.DetailNoteActivity
import com.notemates.ui.detail.user.follows.FollowsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ProfileViewModel>()
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var menuProvider: MenuProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMenu()
        initRecyclerView()
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

                    ProfileUiState.Action.Delete -> {
                        if (status == StateStatus.Failure)
                            Utils.showSnackbar(binding.root, message)
                        else if (status == StateStatus.Success) {
                            Utils.showSnackbar(binding.root, "Berhasil menghapus catatan!")
                            viewModel.loadProfile()
                        }
                    }
                }
            }
        }

        binding.apply {
            textViewName.text = viewModel.authenticatedUser?.name ?: "No name"
            textViewEmail.text = viewModel.authenticatedUser?.email ?: "No email"

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.loadProfile()
                if (swipeRefreshLayout.isRefreshing)
                    swipeRefreshLayout.isRefreshing = false
            }

//            val gotoFollows = fun() {
//                val idUser = viewModel.authenticatedUser?.id
//                if (idUser != null)
//                    startActivity(Intent(requireContext(), FollowsActivity::class.java).apply {
//                        putExtra("idUser", idUser)
//                    })
//            }
            linearLayoutFollowedBy.setOnClickListener { followsClicked(0) }
            linearLayoutFollowing.setOnClickListener { followsClicked(1) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        requireActivity().removeMenuProvider(menuProvider)
    }

    private fun showProfileUi(response: UserProfileResponse) {
        noteAdapter.setNotes(response.notes)
        binding.apply {
            textViewName.text = response.name
            textViewEmail.text = response.email
            textViewFollowedByCount.text = response.count.followedBy.toString()
            textViewFollowingCount.text = response.count.following.toString()
            textViewNotesCount.text = response.count.notes.toString()
        }
    }

    private fun initRecyclerView() {
        noteAdapter = NoteAdapter(
            NoteAdapter.Type.Profile,
            onItemClick = {
                startActivity(Intent(requireContext(), DetailNoteActivity::class.java).apply {
                    putExtra("idNote", it)
                })
            },
            onItemLongClick = {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Hapus Catatan")
                    .setMessage("Apakah Anda yakin akan menghapus catatan dengan judul \"${it.title}\"?")
                    .setPositiveButton("Hapus") { dialog, _ ->
                        viewModel.delete(it.id)
                        dialog.dismiss()
                        Utils.showSnackbar(binding.root, "Menghapus catatan...")
                    }
                    .setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
                    .create()
                    .show()
            })
        binding.recyclerView.apply {
            layoutManager = object : LinearLayoutManager(requireContext()) {
                override fun canScrollVertically() = false
            }
            adapter = noteAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun initMenu() {
        menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_profile, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menuLogout -> MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Keluar")
                        .setMessage("Apakah Anda yakin akan keluar dari aplikasi?")
                        .setPositiveButton("Keluar") { _, _ -> viewModel.logout() }
                        .setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
                        .create()
                        .show()
                }
                return true
            }
        }
        requireActivity().addMenuProvider(menuProvider)
    }

    private fun followsClicked(selectedTab: Int) {
        val idUser = viewModel.authenticatedUser?.id
        if (idUser != null)
            startActivity(Intent(requireContext(), FollowsActivity::class.java).apply {
                putExtra("idUser", idUser)
                putExtra("selectedTab", selectedTab)
            })
    }
}