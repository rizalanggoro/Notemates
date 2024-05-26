package com.notemates.ui.write.note.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.notemates.databinding.FragmentCreateNotePreviewBinding
import com.notemates.ui.write.note.CreateNoteViewModel

class CreateNotePreviewFragment : Fragment() {
    private var _binding: FragmentCreateNotePreviewBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<CreateNoteViewModel>(
        ownerProducer = { requireActivity() }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateNotePreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.markdownContent.observe(requireActivity()) {
            viewModel.markwon.setMarkdown(
                binding.textViewMarkdownContainer, it
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}