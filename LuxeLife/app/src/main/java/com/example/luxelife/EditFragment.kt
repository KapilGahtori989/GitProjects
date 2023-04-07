package com.example.luxelife

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class EditFragment : Fragment() {
    private lateinit var editText: EditText
    private lateinit var originalText: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.blurred_fragment_design, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editText = view.findViewById(R.id.edit_Text_input)

        originalText = arguments?.getString("note") ?: ""
        editText.setText(originalText)

        val button = view.findViewById<Button>(R.id.button_ok)
        button.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            val s = editText.text.toString()
            intent.putExtra("newNote", s)
            startActivity(intent)
        }
    }
}
