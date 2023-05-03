package com.example.kith

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.kith.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // User is already logged in, start the home activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        window.statusBarColor = ContextCompat.getColor(this, R.color.transparent)
        val currentNightMode =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK //->nightModeStatus
        setUiTheme(currentNightMode)


   //---------------------------------------------------------------------------------------------
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle back button press here
                finishAffinity()
            }
        }
        // Add the onBackPressedCallback to the activity's onBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    //---------------------------------------------------------------------------------------------

        binding.registerTextView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

//--------------------------------------------------------------------------------------------------
       binding.logInButton.setOnClickListener {
           showProgressBar()
           val email = binding.emailEditText.text.toString()
           val pass = binding.passwordEditText.text.toString()
           if (email.isNotEmpty() && pass.isNotEmpty()) {
               signIn(email,pass)
           }else
           {
               hideProgressBar()
               Toast.makeText(
                   this,
                   "please fill the required Information...",
                   Toast.LENGTH_LONG
               ).show()
           }
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


    private fun signIn(email:String,pass:String) {
        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                hideProgressBar()
                finish()
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
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }
}



