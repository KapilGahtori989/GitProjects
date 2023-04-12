package com.example.luxelife

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.luxelife.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.*

private var clicked = true//to prevent repopulating of note variable on switching nightMode
class MainActivity : AppCompatActivity(), DesignAdapter.OnButtonClickListener,DesignAdapter.OnItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var myAdapter: DesignAdapter

    override fun onButtonClick(note: String) {
        val fragment = EditFragment()
        val bundle = Bundle()
        bundle.putString("note", note)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit()
    }


    override fun onItemClicked(note: String) {
        val fragment = ViewFragment()
        val bundle = Bundle()
        bundle.putString("note", note)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            clicked = true
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_layout, BlurredFragment())
                .addToBackStack(null)
                .commit()
        }

        val newNote = intent.getStringExtra("newNote")

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        var databaseReference: DatabaseReference? = null

        if (userId != null) {
            databaseReference =
                FirebaseDatabase.getInstance().getReference("users/$userId/list")
            val id = databaseReference.push().key!!
            if (newNote != null && clicked && newNote != "") {
                databaseReference.child(id).setValue(Data(id, newNote))
                clicked = false
            }
        }
        myAdapter = databaseReference?.let { DesignAdapter() }!!
        myAdapter.loadData(databaseReference, this@MainActivity)
        myAdapter.setOnButtonClickListener(this@MainActivity)
        myAdapter.setOnItemClickListener(this@MainActivity)

        binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.recyclerView.adapter = myAdapter

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
}
