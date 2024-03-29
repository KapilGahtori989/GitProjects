package com.example.luxelife

import android.app.AlertDialog
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DesignAdapter : RecyclerView.Adapter<DesignAdapter.ViewHolder>() {
    val itemList: ArrayList<Data> = arrayListOf()
    private var itemClickListener: OnItemClickListener? = null
//--------------------------------------------------------------------------------------------------

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

     fun loadData( context: Context) {
         val userId = FirebaseAuth.getInstance().currentUser?.uid
         val databaseReference =
             FirebaseDatabase.getInstance().getReference("users/$userId/list")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                itemList.clear()
                for (i in dataSnapshot.childrenCount - 1 downTo 0) {
                    val itemSnapshot = dataSnapshot.children.elementAt(i.toInt())
                    val id = itemSnapshot.key
                    val note = itemSnapshot.child("note").getValue(String::class.java)
                    if (note != null && id != null) {
                        itemList.add(Data(id, note))
                    }
                }
                notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // handle error here
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
            }
        })
    }
    //--------------------------------------------------------------------------------------------------

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var note: TextView = itemView.findViewById(R.id.edit_Text)
        var delete: ImageView = itemView.findViewById(R.id.delete_note)
        var text: TextView = itemView.findViewById(R.id.edit_Text)
        var view:ConstraintLayout = itemView.findViewById(R.id.view)
        var scrollView:ScrollView = itemView.findViewById(R.id.scroll_View)

        // Function to check if night mode is enabled
         fun isNightModeEnabled(context: Context): Boolean {
            val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            return currentNightMode == Configuration.UI_MODE_NIGHT_YES
        }

        fun deleteRecord(position: Int) {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val databaseReference =
                FirebaseDatabase.getInstance().getReference("users/$userId/list")
                    .child(itemList[position].Id)
            databaseReference.removeValue()
                .addOnSuccessListener {
                    notifyDataSetChanged()
                }
                .addOnFailureListener {
                    // handle failure here
                    Toast.makeText(
                        itemView.context,
                        "Failed to delete item.$it",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_design, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.note.text = itemList[position].Note

        // Check if night mode is enabled
        val isNightModeEnabled = holder.isNightModeEnabled(holder.itemView.context)
        if (isNightModeEnabled) {
            holder.note.setTextColor(ContextCompat.getColor(holder.itemView.context,R.color.grey))
            holder.delete.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context,R.drawable.baseline_delete_24_white))
        } else {
            holder.note.setTextColor(ContextCompat.getColor(holder.itemView.context,R.color.lightblack))
            holder.scrollView.setBackgroundResource(R.drawable.cardview_background_white)
        }


        holder.delete.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setMessage("Are you sure you want to permanently delete this note?")
                .setPositiveButton("Yes") { _, _ ->
                    holder.deleteRecord(position)
                }
                .setNegativeButton("No", null)
                .show()
        }

        holder.view.setOnClickListener {
            itemClickListener?.onItemClicked(itemList[position].Note,itemList[position].Id)
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(note: String,key:String)
    }

}

