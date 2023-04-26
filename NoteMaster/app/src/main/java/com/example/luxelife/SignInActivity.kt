package com.example.luxelife

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.luxelife.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle back button press here
                finishAffinity()
            }
        }

        // Add the onBackPressedCallback to the activity's onBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        val currentNightMode =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK //->nightModeStatus
        changingTheme(currentNightMode)

        firebaseAuth = FirebaseAuth.getInstance()

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // User is already logged in, start the home activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.textView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener {
            showProgressBar()
            val email = binding.EmailEt.text.toString()
            val pass = binding.PasswordEt.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        hideProgressBar()
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
                            errorMessage.contains("There is no user record corresponding to this identifier. The user may have been deleted.") -> {
                                Toast.makeText(
                                    this,
                                    "There is no user record corresponding to this identifier. The user may have been deleted.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            errorMessage.contains("The password is invalid or the user does not have a password.") -> {
                                Toast.makeText(
                                    this,
                                    "The password is invalid or the user does not have a password.",
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
                Toast.makeText(
                    this,
                    "please fill the required Information...",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun changingTheme(currentNightMode: Int) {
        if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
            // Light mode
        } else {
            // Dark mode
            binding.backgroundSignIn.setBackgroundResource(R.drawable.logindark)
            binding.textView.setTextColor(ContextCompat.getColor(this, R.color.yellow))
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    // Hide the ProgressBar
    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }
}
