package com.example.drugsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.drugsapp.R
import com.example.drugsapp.models.EmployeeDataClass

class EmpAdapter(private val empList:ArrayList<EmployeeDataClass>):RecyclerView.Adapter<EmpAdapter.ViewHolder>(){

    private lateinit var  mListener:OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener){
        mListener = clickListener
    }



    inner class ViewHolder(itemView:View,clickListener: OnItemClickListener):RecyclerView.ViewHolder(itemView){
        val empName:TextView = itemView.findViewById(R.id.text_view_emp_list_item)

        init {
            itemView.setOnClickListener{
                clickListener.onItemClick(bindingAdapterPosition)
            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.emp_list_item,parent,false)
        return ViewHolder(itemView,mListener)
    }

    override fun getItemCount(): Int {
       return empList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentEmp = empList[position]
        holder.empName.text = currentEmp.empName
    }



}