package com.example.albums

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.io.File


class CustomAdapter(private var data: ArrayList<File>, private val context: Context) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val img: ImageView

        init {
            img = itemView.findViewById(R.id.icon)
        }

        fun bind(position: Int) {


            Glide.with(context).load(data[position]).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(img)

            img.setOnClickListener {
                val intent  = Intent(context,ImageShow::class.java)
                intent.putExtra("key",data[position].path)

                Toast.makeText(context, data[position].path, Toast.LENGTH_SHORT).show()
                startActivity(context,intent,null)
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
    }
}