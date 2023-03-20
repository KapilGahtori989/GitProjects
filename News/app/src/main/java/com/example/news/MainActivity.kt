package com.example.news

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.news.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {
    private lateinit var mAdapter: NewsListAdapter
    private lateinit var binding: ActivityMainBinding
    private var category: String = "business"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        category = intent.getStringExtra("category") ?: "business"
        binding.horizontalRe.setOnClickListener {
            val thisCategory = intent.getStringExtra("category") ?: "business"
            if (thisCategory != category) {
                category = thisCategory
                val intent = Intent(this@MainActivity, MainActivity::class.java)
                startActivity(intent)
                finish() // Optional - this will close the current instance of MainActivity
            }
        }
        //---------------------------------------------------------------------------------
        val categories = arrayOf(
            "business",
            "entertainment",
            "general",
            "health",
            "science",
            "sports",
            "technology"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.spinner.setSelection(categories.indexOf(category))


        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val thisCategory = parent?.getItemAtPosition(position).toString()
                // binding.spinner.contentDescription = thisCategory
                // Restart the same activity with the selected category value assigned to the "category" variable.
                if (thisCategory != category) {
                    val intent = Intent(this@MainActivity, MainActivity::class.java)
                    intent.putExtra("category", thisCategory)
                    startActivity(intent)
                    finish() // Optional - this will close the current instance of MainActivity
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {/* Do nothing*/ }
        }
        //---------------------------------------------------------------------------------
        val data = ArrayList<Data>()
        data.add(Data(R.drawable.business, "business"))
        data.add(Data(R.drawable.entertainment, "entertainment"))
        data.add(Data(R.drawable.general, "general"))
        data.add(Data(R.drawable.health, "health"))
        data.add(Data(R.drawable.science, "science"))
        data.add(Data(R.drawable.sports, "sports"))
        data.add(Data(R.drawable.technology, "technology"))

        val currentNightMode =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK //->nightModeStatus
        changingTheme(currentNightMode)

        binding.horizontalRe.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.horizontalRe.adapter = DesignAdapter(data, category)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        fetch(category)
        mAdapter = NewsListAdapter()
        binding.recyclerView.adapter = mAdapter
    }

    private fun changingTheme(currentNightMode: Int) {
        if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
            // Light mode
            val window = window
            window.statusBarColor = ContextCompat.getColor(this, R.color.gray)

        } else {
            // Dark mode
            window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        }
    }

    private fun fetch(category: String) {
     val url = "https://saurav.tech/NewsAPI/top-headlines/category/$category/in.json"

        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val newsJsonArray = response.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for (i in 0 until newsJsonArray.length()) {
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage")
                    )
                    newsArray.add(news)
                }
                mAdapter.updateNews(newsArray)
            },
            { error ->
                Toast.makeText(this, "Error:${error.message}", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(jsonObjectRequest)
    }
}