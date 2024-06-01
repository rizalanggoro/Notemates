package com.notemates.ui.detail.note

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.notemates.core.utils.StateStatus
import com.notemates.core.utils.Utils
import com.notemates.data.models.Note
import com.notemates.databinding.ActivityDetailNoteBinding
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon
import io.noties.markwon.image.glide.GlideImagesPlugin
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailNoteBinding
    private val viewModel by viewModels<DetailNoteViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val idNote = intent.extras?.getInt("idNote")
        if (idNote == null) finish()

        viewModel.getNoteById(idNote!!)

        lifecycleScope.launch {
            viewModel.uiState.collect {
                val (status, message) = it
                if (status == StateStatus.Loading)
                    showLoadingUi(true)
                else {
                    showLoadingUi(false)
                    if (status == StateStatus.Failure)
                        Utils.showSnackbar(binding.root, message)
                    else
                        if (it.response != null)
                            showDetailUi(it.response)
                }
            }
        }

        binding.apply {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getNoteById(idNote)
                if (swipeRefreshLayout.isRefreshing)
                    swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun showDetailUi(response: Note) {
        binding.apply {
            textViewTitle.text = response.title
            textViewDescription.text = response.description
            textViewUser.text = response.user.name

            val markwon = Markwon.builder(applicationContext)
                .usePlugin(GlideImagesPlugin.create(applicationContext))
                .build()
            markwon.setMarkdown(textViewContent, response.content)
        }
    }

    private fun showLoadingUi(isLoading: Boolean) {
        binding.apply {
            progressIndicator.isVisible = isLoading
            content.isVisible = !isLoading
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}