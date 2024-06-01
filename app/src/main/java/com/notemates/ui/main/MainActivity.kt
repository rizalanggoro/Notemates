package com.notemates.ui.main

import android.content.Intent
import android.os.Bundle
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
import com.notemates.ui.auth.AuthActivity
import com.notemates.ui.write.note.CreateNoteActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        if (viewModel.authenticatedUser == null) {
            startActivity(Intent(applicationContext, AuthActivity::class.java))
            finish()
        }

        initNavController()

        binding.apply {
            fabCreateNote.setOnClickListener {
                startActivity(
                    Intent(
                        applicationContext,
                        CreateNoteActivity::class.java
                    )
                )
            }
        }


//        startActivity(Intent(this, DetailUserActivity::class.java).apply {
//            putExtra("idUser", 1)
//        })
    }

    private fun initNavController() {
        binding.apply {
            setSupportActionBar(toolbar)
            val navController = findNavController(R.id.fragment_container_view)
            val bottomNavigationView = bottomNavigationView
            bottomNavigationView.setupWithNavController(navController)

            navController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.profile_fragment -> {
                        fabCreateNote.hide()
                    }

                    else -> {
                        fabCreateNote.show()
                    }
                }
            }
            setupActionBarWithNavController(
                navController, AppBarConfiguration(
                    setOf(
                        R.id.dashboard_fragment,
                        R.id.explore_fragment,
                        R.id.trending_fragment,
                        R.id.profile_fragment,
                    )
                )
            )
        }
    }
}