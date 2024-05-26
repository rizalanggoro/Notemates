package com.notemates.ui.write.note.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.notemates.databinding.FragmentCreateNoteContentBinding
import com.notemates.ui.write.note.CreateNoteViewModel
import io.noties.markwon.editor.MarkwonEditorTextWatcher
import java.util.concurrent.Executors

class CreateNoteContentFragment : Fragment() {
    private var _binding: FragmentCreateNoteContentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateNoteViewModel by viewModels(
        ownerProducer = { requireActivity() }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateNoteContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editTextContent.doAfterTextChanged {
            viewModel.markdownContent.value = it.toString()
        }
        binding.editTextContent.addTextChangedListener(
            MarkwonEditorTextWatcher.withPreRender(
                viewModel.markwonEditor,
                Executors.newCachedThreadPool(),
                binding.editTextContent,
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}