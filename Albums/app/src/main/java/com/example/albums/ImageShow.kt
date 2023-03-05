package com.example.albums

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.FileProvider
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jsibbold.zoomage.ZoomageView
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class ImageShow : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_show)

        val img = findViewById<ZoomageView>(R.id.image_view_image_show)
        val sh = findViewById<CardView>(R.id.share)
        val ca = findViewById<CardView>(R.id.camera_animation)
        val da = findViewById<CardView>(R.id.delete)
        val file = intent.getStringExtra("key")

        Glide.with(this).load(file?.let { File(it) })
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true).into(img)

        sh.setOnClickListener {
            val lottie = findViewById<LottieAnimationView>(R.id.animation)
            lottie.playAnimation() // Play the Lottie animation

            Handler(Looper.getMainLooper()).postDelayed({
                if (file != null) {
                    openingShare(file, this)
                }
            }, lottie.duration) // Delay the function call until the animation is finished
        }

        ca.setOnClickListener {
            openCamera()
        }
        da.setOnClickListener {
            showDeleteConfirmationDialog(file!!)
        }
    }

    private fun showDeleteConfirmationDialog(filePath: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete file")
            .setMessage("Are you sure you want to permanently delete this file?")
            .setPositiveButton("Delete") { _, _ ->//dialog,which --ideally
                // Call your delete function here
                recycle(filePath)
            }
            .setNegativeButton("Cancel") { _, _ ->
                // Do nothing
            }
        val dialog = builder.create()
        dialog.show()
    }


    private fun recycle(filePath: String) {
        val file = File(filePath)
        val success = file.delete()
        if (success) {
            Toast.makeText(this, "File deleted successfully", Toast.LENGTH_SHORT).show()
            // refresh the activity
            val intent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent)
        } else {
            Toast.makeText(
                this,
                "Failed to delete file \n ManageAllFiles permission needed",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // Handle the result of the picture taking here
            Toast.makeText(this, "captured....", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent)
        }
    }

    private fun openCamera() {
        val storageDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/Camera")
        storageDir.mkdirs()

        // Create a unique filename for the captured image
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFile = File(storageDir, "IMG_${timestamp}.jpg")

        // Create an intent to capture a photo and save it to the specified file
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(
            MediaStore.EXTRA_OUTPUT,
            FileProvider.getUriForFile(this, "com.example.albums.provider", imageFile)
        )

        try {
            takePictureLauncher.launch(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Error: " + e.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }


    private fun openingShare(filePath: String, context: Context) {
        val imageFile = File(filePath)
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", imageFile)

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val shareIntent = Intent.createChooser(intent, "Share Image")
        context.startActivity(shareIntent)

    }


}