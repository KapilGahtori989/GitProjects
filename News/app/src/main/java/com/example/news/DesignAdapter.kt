package com.example.news

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView

class DesignAdapter(private val data: ArrayList<Data>, private var category: String) :
    RecyclerView.Adapter<DesignAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ImageView = itemView.findViewById(R.id.img_hor)
        var txt: TextView = itemView.findViewById(R.id.txt_hor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.design_horizontal, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data[position]
        holder.img.setImageResource(currentItem.image)
        holder.txt.text = currentItem.text

        if (category == currentItem.text) {
            //change color here
            holder.txt.setTextColor(getColor(holder.itemView.context, R.color.orange))
        } else {
            holder.txt.setTextColor(getColor(holder.itemView.context, R.color.white))
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, MainActivity::class.java)
            intent.putExtra("category", currentItem.text)
            holder.itemView.context.startActivity(intent)
        }
    }
}