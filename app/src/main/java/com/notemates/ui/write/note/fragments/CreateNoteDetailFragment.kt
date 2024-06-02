package com.notemates.ui.write.note.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.notemates.databinding.FragmentCreateNoteDetailBinding
import com.notemates.ui.write.note.CreateNoteViewModel

class CreateNoteDetailFragment : Fragment() {
    private var _binding: FragmentCreateNoteDetailBinding? = null
    private val binding get() = _binding!!

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

        binding.apply {
            viewModel.authenticatedUser.let {
                if (it != null) {
                    textViewName.text = it.name
                    textViewEmail.text = it.email
                }
            }

            buttonPublish.setOnClickListener {
                val title = editTextTitle.text.toString()
                val description = editTextDescription.text.toString()
                viewModel.publish(title, description)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}