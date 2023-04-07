package com.example.luxelife

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.util.zip.Inflater

class ViewFragment:Fragment() {
    private lateinit var text: TextView
    private lateinit var originalText: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.new_fragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text = view.findViewById(R.id.edit_Text_input)
        text.setTextIsSelectable(true) // allow the text to be selected
        originalText = arguments?.getString("note") ?: ""
        text.text = originalText

        val button = view.findViewById<Button>(R.id.button_ok)
        button.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
    }
}