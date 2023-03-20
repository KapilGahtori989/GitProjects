package com.example.music

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.music.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //changing color of status Bar
        val window = window
        window.statusBarColor = ContextCompat.getColor(this,R.color.firstActivity)

        //delaying activity i.e animation
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, Albums::class.java)
            startActivity(intent)
        }, 1000)

    }
}