package com.example.albums

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.albums.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {

    //GIVING FILES PERMISSION HERE------------------------------------------------------------------
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            } else {
                Log.i("Permission: ", "Denied")
            }
        }

    private fun givingPermissionStorage() {

        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                Toast.makeText(this, R.string.permission_granted, Toast.LENGTH_LONG).show()
            }


            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                Toast.makeText(this, R.string.permission_required, Toast.LENGTH_LONG).show()
                requestPermissionLauncher.launch(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }

            else -> {
                requestPermissionLauncher.launch(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
        }
    }

    private fun givingCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                //Toast.makeText(this, R.string.permission_granted, Toast.LENGTH_LONG).show()
            }


            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                Toast.makeText(this, R.string.permission_requiredC, Toast.LENGTH_LONG).show()
                requestPermissionLauncher.launch(
                    android.Manifest.permission.CAMERA
                )

            }

            else -> {
                requestPermissionLauncher.launch(
                    android.Manifest.permission.CAMERA
                )

            }
        }
    }

    //----------------------------------------------------------------------------------------------
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        givingPermissionStorage()
        givingCameraPermission()

        //refreshing Activity
        val swipeRefreshLayout =findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            finish()
            startActivity(intent)
        }

        // Obtain the internal storage directory
        val storageDir = Environment.getExternalStorageDirectory()

        // Obtain the folder containing the JPG files
        val picturesDir = File(storageDir, "")

        // Create an ArrayList to store the JPG files
        val jpgFiles = ArrayList<File>()

        searchImageFile(picturesDir, jpgFiles)
        //sorting photos to get sorted images i.e last clicked first
        jpgFiles.sortWith { file1, file2 ->
            when {
                file1.lastModified() > file2.lastModified() -> -1
                file1.lastModified() < file2.lastModified() -> 1
                else -> 0
            }
        }


        binding.recyclerView.layoutManager = GridLayoutManager(this, 4)
        binding.recyclerView.adapter = CustomAdapter(jpgFiles, this)
    }

    private fun searchImageFile(directory: File, data: ArrayList<File>) {
        directory.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                // If the file is a directory, recursively search for music files in it
                searchImageFile(file, data)
            } else if (file.isFile && (file.name.endsWith(".jpg") || file.name.endsWith(".jpeg") || file.name.endsWith(
                    ".png"
                ))
            ) {
                // If the file is a music file, add it to the ArrayList
                data.add(file)
            }
        }
    }
}


