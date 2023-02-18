package com.example.drugsapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.drugsapp.models.EmployeeDataClass
import com.example.drugsapp.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Insertion : AppCompatActivity() {
    private lateinit var etEmpName:EditText
    private lateinit var etEmpAge:EditText
    private lateinit var etEmpSalary:EditText
    private lateinit var btnSaveData:Button

    private lateinit var dbRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        etEmpName = findViewById(R.id.edit_text_name)
        etEmpAge = findViewById(R.id.edit_text_age)
        etEmpSalary = findViewById(R.id.edit_text_salary)
        btnSaveData = findViewById(R.id.button_save_data_insertion)

        dbRef = FirebaseDatabase.getInstance().getReference("Employee")

        btnSaveData.setOnClickListener{
            saveEmployeeData()
        }

    }

    private fun saveEmployeeData() {
        //getting values
        val empName = etEmpName.text.toString()
        val empAge = etEmpAge.text.toString()
        val empSalary = etEmpSalary.text.toString()
        val empID = dbRef.push().key!!

        if(empName.isEmpty()){
            etEmpSalary.error = "Please Enter Name"
        }

        if(empAge.isEmpty()){
            etEmpSalary.error = "Please Enter Age"
        }
        if(empSalary.isEmpty()){
            etEmpSalary.error = "Please Enter Salary"
        }


        if(empName.isNotEmpty() && empAge.isNotEmpty() && empSalary.isNotEmpty()){

            val employee = EmployeeDataClass(empID,empName,empAge,empSalary)
            dbRef.child(empID).setValue(employee)
                .addOnCompleteListener{
                    Toast.makeText(this,"Data inserted successfully",Toast.LENGTH_LONG).show()

                    etEmpName.text.clear()
                    etEmpAge.text.clear()
                    etEmpSalary.text.clear()
                }.addOnFailureListener{
                        err->Toast.makeText(this,"Error ${err.message}",Toast.LENGTH_LONG).show()
                }
        }else{
            Toast.makeText(this,"insert data in all fields",Toast.LENGTH_LONG).show()
        }
    }
}