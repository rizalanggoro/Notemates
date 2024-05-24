package com.notemates.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.ObservableBoolean
import arrow.core.Either
import com.notemates.R
import com.notemates.databinding.ActivityAuthenticationBinding
import com.notemates.repositories.AuthRepository
import com.notemates.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AuthenticationActivity : AppCompatActivity(), OnClickListener {
    private lateinit var binding: ActivityAuthenticationBinding

    private val isLogin = ObservableBoolean(true)
    private val isLoading = ObservableBoolean(false)

    @Inject
    lateinit var authRepository: AuthRepository

    private val TAG = "AuthenticationActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.isLogin = isLogin
        binding.isLoading = isLoading

        setSupportActionBar(binding.toolbar)

        binding.buttonSwitch.setOnClickListener(this)
        binding.buttonSubmit.setOnClickListener(this)
    }

    private fun redirectToMainActivity() =
        startActivity(Intent(applicationContext, MainActivity::class.java)).also {
            finish()
        }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_switch -> isLogin.set(!isLogin.get())

            R.id.button_submit -> {
                val name = binding.editTextName.text.toString().trim()
                val email = binding.editTextEmail.text.toString().lowercase().trim()
                val password = binding.editTextPassword.text.toString().lowercase().trim()
                val confirmPassword =
                    binding.editTextConfirmPassword.text.toString().lowercase().trim()

                if (isLogin.get()) {
                    isLoading.set(true)
                    CoroutineScope(Dispatchers.IO).launch {
                        val result = authRepository.login(email, password)

                        launch(Dispatchers.Main) {
                            isLoading.set(false)

                            when (result) {
                                is Either.Left -> Toast.makeText(
                                    applicationContext,
                                    result.value.message,
                                    Toast.LENGTH_SHORT
                                ).show()

                                is Either.Right -> {
                                    Toast.makeText(
                                        applicationContext,
                                        "Success",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    redirectToMainActivity()
                                }
                            }
                        }
                    }
                } else {
                    isLoading.set(true)
                    CoroutineScope(Dispatchers.IO).launch {
                        val result = authRepository.register(name, email, password, confirmPassword)

                        launch(Dispatchers.Main) {
                            isLoading.set(false)

                            when (result) {
                                is Either.Left -> Toast.makeText(
                                    applicationContext,
                                    result.value.message,
                                    Toast.LENGTH_SHORT
                                ).show()

                                is Either.Right -> {
                                    Toast.makeText(
                                        applicationContext,
                                        "Success",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    redirectToMainActivity()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}