package com.notemates.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.notemates.R
import com.notemates.core.utils.StateStatus
import com.notemates.core.utils.Utils
import com.notemates.databinding.ActivityAuthBinding
import com.notemates.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)

        lifecycleScope.launch {
            viewModel.uiState.collect {
                val (action, status, message) = it
                when (action) {
                    AuthUiAction.Initial -> {}
                    AuthUiAction.Login -> {
                        if (status == StateStatus.Loading)
                            showLoadingUi(true)
                        else {
                            showLoadingUi(false)
                            if (status == StateStatus.Success)
                                redirectToMainActivity()
                            else if (status == StateStatus.Failure)
                                Utils.showSnackbar(binding.root, message)
                        }
                    }

                    AuthUiAction.Register -> {
                        if (status == StateStatus.Loading)
                            showLoadingUi(true)
                        else {
                            showLoadingUi(false)
                            if (status == StateStatus.Success)
                                redirectToMainActivity()
                            else if (status == StateStatus.Failure)
                                Utils.showSnackbar(binding.root, message)
                        }
                    }

                    AuthUiAction.SwitchAuthMode -> {
                        binding.textInputLayoutName.isVisible = !it.isLogin
                        binding.textInputLayoutConfirmPassword.isVisible = !it.isLogin
                        binding.buttonSubmit.text = getString(
                            if (it.isLogin) R.string.login
                            else R.string.register
                        )
                        binding.buttonSwitch.text = getString(
                            if (it.isLogin) R.string.switch_to_register
                            else R.string.switch_to_login
                        )
                    }
                }
            }
        }

        binding.buttonSwitch.setOnClickListener { viewModel.switchMode() }
        binding.buttonSubmit.setOnClickListener {
            val name = binding.editTextName.text.toString().trim()
            val email = binding.editTextEmail.text.toString().lowercase().trim()
            val password = binding.editTextPassword.text.toString().lowercase().trim()
            val confirmPassword = binding.editTextConfirmPassword.text.toString()
                .lowercase().trim()

            if (viewModel.uiState.value.isLogin) {
                viewModel.login(
                    email,
                    password,
                )
            } else {
                viewModel.register(
                    name,
                    email,
                    password,
                    confirmPassword,
                )
            }
        }
    }

    private fun showLoadingUi(isLoading: Boolean) {
        binding.progressIndicator.isVisible = isLoading
        binding.buttonSubmit.isEnabled = !isLoading
        binding.buttonSwitch.isEnabled = !isLoading
    }

    private fun redirectToMainActivity() =
        startActivity(Intent(applicationContext, MainActivity::class.java)).also {
            finish()
        }
}