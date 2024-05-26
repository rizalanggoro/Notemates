package com.notemates.ui.write.note

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.notemates.databinding.ActivityCreateNoteBinding
import com.notemates.ui.write.note.adapters.CreateNoteFragmentStateAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateNoteBinding

    private val viewModel by viewModels<CreateNoteViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCreateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragmentStateAdapter = CreateNoteFragmentStateAdapter(this)
        binding.viewPager.adapter = fragmentStateAdapter
        binding.viewPager.isUserInputEnabled = false
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = getString(CreateNoteFragmentStateAdapter.titles[position])
        }.attach()

//        val markwon = Markwon.create(applicationContext)
//        val markwonEditor = MarkwonEditor.create(markwon)

//        binding.editTextEditor.addTextChangedListener(
//            MarkwonEditorTextWatcher.withPreRender(
//                markwonEditor,
//                Executors.newCachedThreadPool(),
//                binding.editTextEditor,
//            )
//        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }
}