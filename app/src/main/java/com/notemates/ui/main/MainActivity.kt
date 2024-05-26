package com.notemates.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.notemates.R
import com.notemates.databinding.ActivityMainBinding
import com.notemates.ui.AuthenticationActivity
import com.notemates.ui.write.note.CreateNoteActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnClickListener {
    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (!viewModel.isAuthenticated) {
            startActivity(Intent(applicationContext, AuthenticationActivity::class.java))
            finish()
        }

        setSupportActionBar(binding.toolbar)

        val bottomNavigationView = binding.bottomNavigationView
        val navController = findNavController(R.id.fragment_container_view)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.profile_fragment -> binding.fabCreateNote.hide()
                else -> binding.fabCreateNote.show()
            }
        }

        setupActionBarWithNavController(
            navController, AppBarConfiguration(
                setOf(
                    R.id.home_fragment,
                    R.id.explore_fragment,
                    R.id.trending_fragment,
                    R.id.profile_fragment,
                )
            )
        )
        bottomNavigationView.setupWithNavController(navController)

        binding.fabCreateNote.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_create_note -> startActivity(
                Intent(
                    applicationContext,
                    CreateNoteActivity::class.java
                )
            )
        }
    }
}