package com.example.kith

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.kith.databinding.ActivityUserDetailsBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserDetails : AppCompatActivity() {
    private lateinit var binding:ActivityUserDetailsBinding
    private lateinit var dbRef:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        setUiTheme(currentNightMode)

        binding.submitUserDetails.setOnClickListener {
            val email = intent.getStringExtra("email")
            val uid = intent.getStringExtra("uid")
            var name = binding.userNameUserDetails.text.toString()
            if(name==""){
                name = email!!
            }
            addUserToDatabase(name,email,uid)
            val intent = Intent(this,SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun addUserToDatabase(name: String, email: String?, uid: String?) {
        dbRef = FirebaseDatabase.getInstance().reference
        dbRef.child("user").child(uid!!).setValue(User(name,email, uid))
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
            //navigation bar is bottom bar
            window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        }
    }
}