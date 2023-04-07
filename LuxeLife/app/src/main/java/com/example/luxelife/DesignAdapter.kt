package com.example.luxelife

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.DelicateCoroutinesApi

class DesignAdapter(c: Context) : RecyclerView.Adapter<DesignAdapter.ViewHolder>() {
    val itemList: ArrayList<Data> = arrayListOf()
    private lateinit var dbRef: DatabaseReference
    private lateinit var c: Context
    private var buttonClickListener: OnButtonClickListener? = null

//--------------------------------------------------------------------------------------------------

    fun setOnButtonClickListener(listener: OnButtonClickListener) {
        buttonClickListener = listener
    }

    suspend fun loadData(databaseReference: DatabaseReference, context: Context) {
        dbRef = databaseReference
        c = context
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
        var update: ImageView = itemView.findViewById(R.id.edit_note)

        @OptIn(DelicateCoroutinesApi::class)
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
        holder.delete.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setMessage("Are you sure you want to permanently delete this note?")
                .setPositiveButton("Yes") { dialog, which ->
                    holder.deleteRecord(position)
                }
                .setNegativeButton("No", null)
                .show()
        }

        holder.update.setOnClickListener {
            buttonClickListener?.onButtonClick(itemList[position].Note)
            holder.deleteRecord(position)
        }
    }

    interface OnButtonClickListener {
        fun onButtonClick(note: String)
    }
}

