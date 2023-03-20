package com.example.music

import android.content.Intent
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class MusicAdapter(var data:ArrayList<File>, var currentNightMode: Int): RecyclerView.Adapter<MusicAdapter.ViewHolder>() {

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        private var textView:TextView = view.findViewById(R.id.text_view_design)

        init {
            //SETTING COLOR TO THE LIST ITEMS TEXT
            if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
                // Light mode
                textView.setTextColor(ContextCompat.getColor(view.context, R.color.black))
            } else {
                // Dark mode
                textView.setTextColor(ContextCompat.getColor(view.context, R.color.white))
            }
        }

        fun setData(position: Int){
            textView.text = data[position].name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.design,parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.setData(position)
        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context,MusicPlayer::class.java)
            intent.putExtra("filePath",data[position].path)

            // Start the MusicPlayer activity
            holder.itemView.context.startActivity(intent)
            holder.itemView.context.startActivity(intent)
        }
    }

}