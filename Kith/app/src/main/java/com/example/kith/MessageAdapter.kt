package com.example.kith

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(private val context:Context, private val messageList:ArrayList<Message>): RecyclerView.Adapter<ViewHolder>() {

    private val itemReceive = 1
    private val itemSent = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if(viewType==1){
            //inflate receive
            val view  = LayoutInflater.from(context).inflate(R.layout.receive,parent,false)
            ReceiveViewHolder(view)
        }else{
            //inflate sent
            val view  = LayoutInflater.from(context).inflate(R.layout.sent,parent,false)
            SentViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        if(holder.javaClass==SentViewHolder::class.java){
            //do the stuff for sent

            val viewHolder = holder as SentViewHolder
            viewHolder.sentMessage.text =currentMessage.message
        }else{
            //do for receive View Holder
            val viewHolder = holder as ReceiveViewHolder
            viewHolder.receiveMessage.text =currentMessage.message
        }
    }


    class SentViewHolder(itemView: View):ViewHolder(itemView){
        val sentMessage: TextView = itemView.findViewById(R.id.sent_message)
    }

    class ReceiveViewHolder(itemView: View):ViewHolder(itemView){
        val receiveMessage: TextView = itemView.findViewById(R.id.receive_message)

    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        return if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            itemSent
        }else{
            itemReceive
        }
    }

}