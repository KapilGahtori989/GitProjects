package com.example.kith

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserListAdapter(private val context:Context, private val userList:ArrayList<User>):RecyclerView.Adapter<UserListAdapter.ViewHolder>() {
inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
val text:TextView =itemView.findViewById(R.id.name_user_list_of_people)
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
     val view = LayoutInflater.from(parent.context).inflate(R.layout.design_list_of_people,parent,false)
     return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentUser=  userList[position]
        holder.text.text = currentUser.name!!

        holder.itemView.setOnClickListener {
            val intent = Intent(context,ChatActivity::class.java)
            intent.putExtra("name",currentUser.name)
            intent.putExtra("uid",currentUser.uid)
            context.startActivity(intent)
        }
    }

}