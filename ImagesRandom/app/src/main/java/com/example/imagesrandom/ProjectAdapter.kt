package com.example.imagesrandom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ProjectAdapter(private var url:String,private val identifierList:List<String>, private val mLayoutManager: LinearLayoutManager):RecyclerView.Adapter<ProjectAdapter.ViewHolder>() {

    private lateinit var image:ImageView
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        init {
            image = itemView.findViewById(R.id.image_view_design)
        }
         fun bind(position: Int){
             val identifier = identifierList[position]
             val apiUrl = "$url?identifier=$identifier"
             Glide.with(itemView.context).
             load(apiUrl).
             diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).
             into(image)
         }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.design,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return 1000
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }


}