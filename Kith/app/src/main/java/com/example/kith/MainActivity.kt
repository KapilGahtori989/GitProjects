package com.example.kith

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kith.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserListAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showProgressBar()
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        setUiTheme(currentNightMode)
        dbRef = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()
        userList = ArrayList()

        // Get a reference to the ActionBar object
        val actionBar = supportActionBar

        // Set the Action Bar color to transparent
        actionBar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this,
                    R.color.transparent
                )
            )
        )

        dbRef.child("user").addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (postSnapshot in snapshot.children) {
                    val currentUser = postSnapshot.getValue(User::class.java)
                    if (mAuth.currentUser?.uid != currentUser?.uid) {
                        userList.add(currentUser!!)
                        adapter.notifyDataSetChanged()
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        adapter = UserListAdapter(this, userList)
        binding.recyclerViewListOfPeople.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewListOfPeople.adapter = adapter

        //-----------------------------------------------------------------------------------------
        // Hide progress bar after adapter has finished loading data
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {

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
            //Light Mode
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val wic = WindowInsetsControllerCompat(window, window.decorView)
                wic.isAppearanceLightStatusBars = true // true or false as desired.
                window.statusBarColor = ContextCompat.getColor(this, R.color.transparent)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = ContextCompat.getColor(this, R.color.transparent)
            }
        } else {
            //Dark Mode
            window.statusBarColor = ContextCompat.getColor(this, R.color.transparent)
            window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        }
    }

    //---------------Menu------------------------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.log_out) {
            val mAuth = FirebaseAuth.getInstance()
            mAuth.signOut()
            val intent = Intent(this@MainActivity, SignInActivity::class.java)
            startActivity(intent)
            finish()
            return true
        }
        return true
    }

    //--------------------------------------------------------------------------------------------
    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

}