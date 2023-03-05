package com.example.albums

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.io.File


class CustomAdapter(private var data: ArrayList<File>, private val context: Context) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView

        init {
            img = itemView.findViewById(R.id.icon)

        }

        fun bind(position: Int) {


            Glide.with(context).load(data[position]).into(img)

            //can use
            //Glide.with(context).load(data[position]).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(img)
            //For removing cache everytime from app

            img.setOnClickListener {
                val intent = Intent(context, ImageShow::class.java)
                intent.putExtra("key", data[position].path)
                startActivity(context, intent, null)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.grid_view, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)

        // Get the screen width in pixels
        val displayMetrics = context.resources.displayMetrics
        val screenWidthPx = displayMetrics.widthPixels

        // Calculate the item width based on the screen width and the number of columns you want to display
        val numColumns = 4 // Replace this with the number of columns you want to display
        val itemWidthPx = screenWidthPx / numColumns

        // Set the item width to the ImageView
        holder.img.layoutParams.width = itemWidthPx
        holder.img.layoutParams.height = itemWidthPx
    }
}