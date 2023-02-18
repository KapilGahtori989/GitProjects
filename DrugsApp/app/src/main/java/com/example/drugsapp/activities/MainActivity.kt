package com.example.drugsapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.drugsapp.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnInsert:Button = findViewById(R.id.button_insert_data)
        val btnFetch:Button = findViewById(R.id.button_fetch_data)
        btnInsert.setOnClickListener{
            val intent = Intent(this, Insertion::class.java)
            startActivity(intent)
        }

        btnFetch.setOnClickListener{
            val intent = Intent(this, FetchingActivity::class.java)
            startActivity(intent)
        }
    }
}