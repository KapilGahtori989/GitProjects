package com.example.luxelife

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.luxelife.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.*

class MainActivity() : AppCompatActivity(), DesignAdapter.OnButtonClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var myAdapter: DesignAdapter

    override fun onButtonClick(note: String) {
        val fragment = EditFragment()
        val bundle = Bundle()
        bundle.putString("note", note)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit()
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searching.postDelayed(
            {
                binding.searching.visibility = View.GONE
            }, 2000
        )
//----------------------------------------------------------------------------------------
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
//-----------------------------------------------------------------------------------------

        binding.logOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.addNotesImageView.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_layout, BlurredFragment())
                .addToBackStack(null)
                .commit()
        }

        val newNote = intent.getStringExtra("newNote")

        GlobalScope.launch(Dispatchers.IO) {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            var databaseReference: DatabaseReference? = null

            if (userId != null) {
                databaseReference =
                    FirebaseDatabase.getInstance().getReference("users/$userId/list")
                val id = databaseReference.push().key!!
                if (newNote != null) {
                    databaseReference.child(id).setValue(Data(id, newNote))
                }

            }
            myAdapter = databaseReference?.let { DesignAdapter(this@MainActivity) }!!
            myAdapter.loadData(databaseReference, this@MainActivity)
            myAdapter.setOnButtonClickListener(this@MainActivity)

            withContext(Dispatchers.Main) {
                binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                binding.recyclerView.adapter = myAdapter
            }
        }
    }
}