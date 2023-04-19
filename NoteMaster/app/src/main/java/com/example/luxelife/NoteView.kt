package com.example.luxelife

import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.luxelife.databinding.ActivityNoteViewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class NoteView : AppCompatActivity() {
    private lateinit var binding:ActivityNoteViewBinding
    var originalNote:String?=null
    var id:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.editTextNoteViewActivity.background = null
        originalNote = intent.getStringExtra("originalNote")
        id = intent.getStringExtra("id")
        binding.editTextNoteViewActivity.setText(originalNote)

        val currentNightMode =
            this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK //->nightModeStatus
        changingTheme(currentNightMode)


        binding.parentLayout.setOnClickListener{
            binding.editTextNoteViewActivity.requestFocus()
            // Show soft keyboard
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(binding.editTextNoteViewActivity, InputMethodManager.SHOW_IMPLICIT)

            // Set cursor to the end of the text
            binding.editTextNoteViewActivity.setSelection(binding.editTextNoteViewActivity.text.length)
        }

    }

    override fun onPause() {
        super.onPause()
        updateData()
    }

    private fun updateData() {
        if(binding.editTextNoteViewActivity.text.toString() != originalNote && binding.editTextNoteViewActivity.text.toString()!=""){
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val dbRef = FirebaseDatabase.getInstance().getReference("users/$userId/list").child(id
                ?: "")
            val data = Data(id ?: "", binding.editTextNoteViewActivity.text.toString())
            dbRef.setValue(data)
        }

        if(binding.editTextNoteViewActivity.text.toString()==""){
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val dbRef = FirebaseDatabase.getInstance().getReference("users/$userId/list").child(id
                ?: "")
            dbRef.removeValue()
            Toast.makeText(this,"empty note discarded",Toast.LENGTH_LONG).show()
        }
    }

    private fun changingTheme(currentNightMode: Int) {
        if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
            // Light mode
            binding.editTextNoteViewActivity.setTextColor(ContextCompat.getColor(this,R.color.lightblack))
            val wic = WindowInsetsControllerCompat(window, window.decorView)
            wic.isAppearanceLightStatusBars = true  // true or false as desired.
            window.statusBarColor = ContextCompat.getColor(this, R.color.transparent)
        } else {
            // Dark mode
            binding.editTextNoteViewActivity.setTextColor(ContextCompat.getColor(this,R.color.grey))
            window.statusBarColor = ContextCompat.getColor(this, R.color.transparent)
        }
    }
}