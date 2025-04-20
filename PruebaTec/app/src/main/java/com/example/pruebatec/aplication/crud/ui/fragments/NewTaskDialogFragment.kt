package com.example.pruebatec.aplication.crud.ui.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.pruebatec.R

class NewTaskDialogFragment(private val onTaskAdded: (String) -> Unit ) : DialogFragment() {
    @SuppressLint("MissingInflatedId")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_new_task, null)
        val etDescription = view.findViewById<EditText>(R.id.et_description)

        return AlertDialog.Builder(requireContext())
            .setTitle("Nueva Tarea")
            .setView(view)
            .setPositiveButton("Agregar") { _, _ ->
                val desc = etDescription.text.toString().trim()
                if (desc.isNotEmpty()) {
                    onTaskAdded(desc)
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()
    }
}