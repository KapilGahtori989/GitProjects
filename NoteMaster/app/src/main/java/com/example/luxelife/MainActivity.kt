package com.example.luxelife

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.luxelife.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), DesignAdapter.OnItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var myAdapter: DesignAdapter

    override fun onItemClicked(note: String, key: String) {
        val intent = Intent(this, NoteView::class.java)
        intent.putExtra("originalNote", note)
        intent.putExtra("id", key)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showProgressBar()

//----------------------------------------------------------------------------------------
        val currentNightMode =
            this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK //->nightModeStatus

        setUiTheme(currentNightMode)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
//-----------------------------------------------------------------------------------------


        binding.logOut.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage("Are you sure you want to log out")
                .setPositiveButton("Yes") { _, _ ->
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("No", null)
                .show()
        }


        binding.addNotesImageView.setOnClickListener {
            val intent = Intent(this, AddNotes::class.java)
            startActivity(intent)
        }

        myAdapter = DesignAdapter()
        myAdapter.loadData(this@MainActivity)
        myAdapter.setOnItemClickListener(this@MainActivity)

        binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.recyclerView.adapter = myAdapter

        //-----------------------------------------------------------------------------------------
        // Hide progress bar after adapter has finished loading data
        myAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                hideProgressBar()
            }
        })
        //-----------------------------------------------------------------------------------------
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
            window.statusBarColor = ContextCompat.getColor(this, R.color.transparent)

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
