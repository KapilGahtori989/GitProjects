package com.example.music

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.music.databinding.ActivityMusicPlayerBinding

class MusicPlayer : AppCompatActivity() {
    private lateinit var binding: ActivityMusicPlayerBinding

    companion object {
        private var mediaPlayer: MediaPlayer? = null
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = window
        window.statusBarColor = ContextCompat.getColor(this, R.color.reddish)

        //Playing Music
        val filePath = intent.getStringExtra("filePath")

        settingImageAlbum(filePath!!)

        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.stop()
        }
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setOnPreparedListener {
            mediaPlayer?.start()
        }

        mediaPlayer!!.setOnPreparedListener {
            mediaPlayer!!.start()
        }

        //Not of Much Importance--------------------------------------------------------------------
        mediaPlayer!!.setOnErrorListener { _, what, extra ->
            Log.e("MediaPlayer", "Error: what=$what, extra=$extra")
            false
        }
        //------------------------------------------------------------------------------------------

        mediaPlayer!!.setDataSource(filePath)
        mediaPlayer!!.prepareAsync()

        binding.fabPause.setOnClickListener {
            mediaPlayer!!.pause()
        }

        binding.fabPlay.setOnClickListener {
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer!!.pause()
            }
            mediaPlayer!!.start()
        }

        binding.fabStop.setOnClickListener {
            mediaPlayer!!.stop()
            val intent = Intent(this,Albums::class.java)
            startActivity(intent)
        }

        //Set up the SeekBar -->SEEKBAR
        binding.seekBar.max = mediaPlayer?.duration ?: 0

        val handler = Handler(Looper.getMainLooper())

        mediaPlayer?.let {
            // update SeekBar and TextView every second
            handler.postDelayed(object : Runnable {
                override fun run() {
                    binding.seekBar.progress = it.currentPosition
                    binding.timeTextView.text = getTimeString(it.currentPosition)
                    binding.timeTextViewEnd.text = getTimeString(it.duration)
                    handler.postDelayed(this, 1000)
                }
            }, 1000)
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer?.seekTo(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })


        //we could use v in turn of _ but we will not use it so i used _
        binding.seekBar.setOnTouchListener { _, event ->
            mediaPlayer?.let {
                binding.seekBar.max = it.duration
                val durationHandler = Handler(Looper.getMainLooper())
                durationHandler.postDelayed(object : Runnable {
                    override fun run() {
                        binding.seekBar.progress = it.currentPosition
                        durationHandler.postDelayed(this, 100)
                    }
                }, 100)
            }

            //pauses music when we touch seekbar and starts when we stop touching it
            mediaPlayer?.let {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        it.pause()
                    }
                    MotionEvent.ACTION_UP -> {
                        it.start()
                    }
                }
            }
            false
        }
    }


    private fun settingImageAlbum(filePath:String) {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(filePath)

        val art = retriever.embeddedPicture
        if (art != null) {
            val bitmap = BitmapFactory.decodeByteArray(art, 0, art.size)
            binding.album.setImageBitmap(bitmap)
        } else {
            // handle case where no album art is found
            binding.album.setImageResource(R.drawable.singer)
        }

    }

    private fun getTimeString(progress: Int): CharSequence {
        val minutes = (progress / 1000) / 60
        val seconds = (progress / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}
