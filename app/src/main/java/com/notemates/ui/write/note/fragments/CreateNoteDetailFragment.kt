package com.notemates.ui.write.note.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.notemates.data.repositories.AuthRepository
import com.notemates.databinding.FragmentCreateNoteDetailBinding
import com.notemates.ui.write.note.CreateNoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateNoteDetailFragment : Fragment() {
    private var _binding: FragmentCreateNoteDetailBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var authRepository: AuthRepository

    private val viewModel: CreateNoteViewModel by viewModels(
        ownerProducer = { requireActivity() }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateNoteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.authenticatedUser = authRepository.authenticatedUser
        binding.counter = viewModel.counter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}