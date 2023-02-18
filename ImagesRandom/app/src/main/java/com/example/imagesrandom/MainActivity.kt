package com.example.imagesrandom
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.security.SecureRandom
import java.util.*


class MainActivity : AppCompatActivity() {

    private var mLayoutManager = LinearLayoutManager(this)
    private val identifierList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recycler: RecyclerView = findViewById(R.id.recycler_view_main)
        val x = "https://random.imagecdn.app/500/150"
        generateIdentifiers()
       recycler.layoutManager = LinearLayoutManager(this)
       recycler.adapter = ProjectAdapter(x,identifierList,mLayoutManager)

    }
    //to generate random images in each view of recycler view
    private fun generateIdentifiers() {
        val random = SecureRandom()
        for (i in 0 until 1000) {
            val currentTime = System.currentTimeMillis()
            val randomNumber = random.nextInt(10000)
            val identifier = "$currentTime$randomNumber"
            identifierList.add(identifier)
        }

    }
    }