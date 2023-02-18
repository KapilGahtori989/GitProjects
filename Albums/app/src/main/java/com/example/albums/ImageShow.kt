package com.example.albums

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jsibbold.zoomage.ZoomageView
import java.io.File

class ImageShow : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_show)

        val img = findViewById<ZoomageView>(R.id.image_view_image_show)
        val sh =findViewById<CardView>(R.id.share)
        val ca = findViewById<CardView>(R.id.camera_animation)
        val file = intent.getStringExtra("key")

        Glide.with(this).load(file?.let { File(it) })
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true).into(img)

            sh.setOnClickListener{
                val lottie = findViewById<LottieAnimationView>(R.id.animation)
                lottie.playAnimation() // Play the Lottie animation

                Handler(Looper.getMainLooper()).postDelayed({
                    if (file != null) {
                        openingShare(file)
                    }
                }, lottie.duration) // Delay the function call until the animation is finished
            }

        ca.setOnClickListener{
           openCamera()
        }
    }

    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            // Handle the result of the picture taking here
            Toast.makeText(this,"captured....",Toast.LENGTH_LONG).show()
        }
    }
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            takePictureLauncher.launch(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Error: " + e.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun openingShare(file:String){
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "file path  :$file")
        startActivity(Intent.createChooser(sharingIntent, "Share via"))

    }
}