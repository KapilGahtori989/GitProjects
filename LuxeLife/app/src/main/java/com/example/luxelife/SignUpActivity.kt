package com.example.luxelife

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.luxelife.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentNightMode =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK //->nightModeStatus
        changingTheme(currentNightMode)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.button.setOnClickListener {
            val email = binding.EmailEt.text.toString()
            val pass = binding.PasswordEt.text.toString()
            val confirmPass = binding.ConfirmPassEt.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, SignInActivity::class.java)
                            startActivity(intent)
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
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Passwords do not match. Please try again.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(this, "Empty Fields are not Allowed !!", Toast.LENGTH_LONG).show()
            }
        }

        binding.textView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    private fun changingTheme(currentNightMode: Int) {
        if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
            // Light mode
            binding.backgroundRegister.setBackgroundResource(R.drawable.registerdark)
            binding.textView.setTextColor(ContextCompat.getColor(this, R.color.yellow))
        } else {
            // Dark mode
        }
    }
}