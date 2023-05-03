package com.example.kith

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kith.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDBRef: DatabaseReference
    private var receiverRoom: String? = null
    private var senderRoom: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        setUiTheme(currentNightMode)
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

        mDBRef = FirebaseDatabase.getInstance().reference
        val name = intent.getStringExtra("name")
        supportActionBar?.title = name
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)
        binding.chatRecylerView.layoutManager = LinearLayoutManager(this)
        binding.chatRecylerView.adapter = messageAdapter

        //logic for adding data to recyclerView
        mDBRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                var i = 0
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue(Message::class.java)
                        i++
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyItemInserted(i)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        //adding the message to database
        binding.sendButton.setOnClickListener {
            val message = binding.messageBox.text.toString()
            val messageObject = Message(message, senderUid)

            mDBRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDBRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            binding.messageBox.setText("")
        }


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
    }}