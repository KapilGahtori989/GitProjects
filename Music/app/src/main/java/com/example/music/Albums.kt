package com.example.music

import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.music.databinding.ActivityAlbumsBinding
import java.io.File

class Albums : AppCompatActivity() {
    private lateinit var binding: ActivityAlbumsBinding
    private lateinit var backPressedCallback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Create callback for back button press
        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Call finishAffinity to close the entire app
                finishAffinity()
            }
        }

        // Add the callback to the OnBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, backPressedCallback)

        //Changing color of the textView according to the theme of android phone
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK //->nightModeStatus
        changingTheme(currentNightMode)
        statusBarColorChange()
        storagePermission()
        val data = initializingData()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = MusicAdapter(data, currentNightMode)
    }

    override fun onDestroy() {
        // Remove the callback if the activity is destroyed
        backPressedCallback.remove()
        super.onDestroy()
    }
    
    //GETTING PERMISSION TO READ EXTERNAL STORAGE IN PHONE
    private fun storagePermission() {
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()){
                isGranted:Boolean->
                if (isGranted){
                    val intent = Intent(this,Albums::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                }else{
                    Log.i("Permission","Denied")
                }
            }

        when{
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )==PackageManager.PERMISSION_GRANTED->{
                Toast.makeText(this,"permission is granted",Toast.LENGTH_LONG).show()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )->{
                Toast.makeText(this,"Storage access is required for showing music files",Toast.LENGTH_LONG).show()
                requestPermissionLauncher.launch(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }

            else->{
                requestPermissionLauncher.launch(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
        }
    }
    private fun initializingData(): ArrayList<File> {
        val data: ArrayList<File> = ArrayList()
        val storageDir = Environment.getExternalStorageDirectory()

        searchMusicFiles(storageDir,data)
        //sorting in terms of date added
        data.sortWith { file1, file2 ->
            when {
                file1.lastModified() > file2.lastModified() -> -1
                file1.lastModified() < file2.lastModified() -> 1
                else -> 0
            }
        }
        return data
    }

    private fun searchMusicFiles(directory: File,data:ArrayList<File>) {
        directory.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                // If the file is a directory, recursively search for music files in it
                searchMusicFiles(file,data)
            } else if (file.isFile && (file.name.endsWith(".mp3") || file.name.endsWith(".m4a"))) {
                // If the file is a music file, add it to the ArrayList
                data.add(file)
            }
        }
    }

    private fun statusBarColorChange() {
        val window = window
        window.statusBarColor = ContextCompat.getColor(this, R.color.reddish)
    }

    private fun changingTheme(currentNightMode: Int) {
        val textView = findViewById<TextView>(R.id.heading)

        if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
            // Light mode
            textView.setTextColor(ContextCompat.getColor(this, R.color.black))
        } else {
            // Dark mode
            textView.setTextColor(ContextCompat.getColor(this, R.color.white))
        }

    }
}