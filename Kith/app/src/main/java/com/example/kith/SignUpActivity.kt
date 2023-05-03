package com.example.kith

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.kith.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this, R.color.transparent)
        val currentNightMode =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK //->nightModeStatus
        setUiTheme(currentNightMode)

        binding.loginTextView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        firebaseAuth = FirebaseAuth.getInstance()

        binding.registerButton.setOnClickListener {
            registerUser()
        }

    }

    private fun registerUser() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        val confirmPass = binding.confirmPasswordEditText.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty() && confirmPass.isNotEmpty()) {
            if (password == confirmPass) {
                showProgressBar()
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, UserDetails::class.java)
                        intent.putExtra("email",email)
                        intent.putExtra("uid",firebaseAuth.currentUser?.uid!!)
                        startActivity(intent)
                        hideProgressBar()
                        Toast.makeText(this, "Successfully registered", Toast.LENGTH_LONG).show()

                    } else {
                        val errorMessage = it.exception.toString()

                        when {
                            errorMessage.contains("The email address is badly formatted.") -> {
                                Toast.makeText(
                                    this,
                                    "The email address is badly formatted.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            errorMessage.contains("The email address is already in use by another account.") -> {
                                Toast.makeText(
                                    this,
                                    "The email address is already in use by another account.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            errorMessage.contains("The given password is invalid. [ Password should be at least 6 characters ]") -> {
                                Toast.makeText(
                                    this,
                                    "The given password is invalid. [ Password should be at least 6 characters ]",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            else -> {
                                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                        hideProgressBar()
                    }
                }
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Empty Fields are not Allowed !!", Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun setUiTheme(currentNightMode: Int) {
        if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
            // Light mode
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val wic = WindowInsetsControllerCompat(window, window.decorView)
                wic.isAppearanceLightStatusBars = true // true or false as desired.
                window.statusBarColor = ContextCompat.getColor(this, R.color.transparent)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = ContextCompat.getColor(this, R.color.transparent)
            }
        } else {
            // Dark mode
            binding.headingText.setTextColor(Color.WHITE)
            //navigation bar is bottom bar
            window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }
}