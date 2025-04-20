package com.example.pruebatec.aplication.crud.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class StatusDialogFragment (private val onStatusSelected: (Int, String) -> Unit) : DialogFragment() {
    private val listStatus = listOf(
        "En pausa",
        "Iniciada",
        "Pausada",
        "En revisión",
        "Finalizada"
    )

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Selecciona el estado")
            .setItems(listStatus.toTypedArray()) { _, which ->
                onStatusSelected(which, listStatus[which])
            }
            .create()
    }
}