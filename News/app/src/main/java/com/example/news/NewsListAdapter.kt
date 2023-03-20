package com.example.news

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class NewsListAdapter : RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {
    private val items: ArrayList<News> = ArrayList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ImageView = itemView.findViewById(R.id.image_view_news)
        var description: TextView = itemView.findViewById(R.id.text_view_news)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.design, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = items[position]
        holder.description.text = currentItem.title
        Glide.with(holder.itemView.context).load(currentItem.imageUrl).into(holder.img)
        holder.itemView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(currentItem.url))
            holder.itemView.context.startActivity(intent)
        }
    }

    fun updateNews(updatedNews: ArrayList<News>) {
        items.addAll(updatedNews)
        notifyDataSetChanged()
    }
}