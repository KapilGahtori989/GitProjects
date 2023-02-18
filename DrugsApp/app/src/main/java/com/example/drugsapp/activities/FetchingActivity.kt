package com.example.drugsapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.inflate
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drugsapp.R
import com.example.drugsapp.adapters.EmpAdapter
import com.example.drugsapp.models.EmployeeDataClass
import com.google.firebase.database.*

class FetchingActivity : AppCompatActivity() {
    private lateinit var empRecyclerView:RecyclerView
    private lateinit var tvLoadingDat:TextView
    private lateinit var empList:ArrayList<EmployeeDataClass>
    private lateinit var dbRef:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching)
        empRecyclerView = findViewById(R.id.rvEmp)
        tvLoadingDat = findViewById(R.id.text_view_fetching)

        empRecyclerView.layoutManager = LinearLayoutManager(this)
        empRecyclerView.setHasFixedSize(true)

        empList = arrayListOf<EmployeeDataClass>()

        getEmployeesData()
    }

    private fun getEmployeesData() {
        empRecyclerView.visibility = View.GONE
        tvLoadingDat.visibility=View.VISIBLE
        dbRef = FirebaseDatabase.getInstance().getReference("Employee")
        dbRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                empList.clear()
                if(snapshot.exists()){//i.e if data exists
                    for(empSnap in snapshot.children){
                        val empData = empSnap.getValue(EmployeeDataClass::class.java)
                        empList.add(empData!!)
                    }
                    val mAdapter = EmpAdapter(empList)
                    empRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object :EmpAdapter.OnItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@FetchingActivity,EmployeeDetails::class.java)

                            //put extra data
                            intent.putExtra("empId",empList[position].empId)
                            intent.putExtra("empName",empList[position].empName)
                            intent.putExtra("empAge",empList[position].empAge)
                            intent.putExtra("empSalary",empList[position].empSalary)
                            startActivity(intent)
                        }

                    })
                    empRecyclerView.visibility = View.VISIBLE
                    tvLoadingDat.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }
}

