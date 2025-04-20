package com.example.pruebatec.aplication.crud.ui

import com.example.pruebatec.models.Tarea

interface OnTaskClickListener {
    fun onTaskDetailClick(tarea: Tarea)
}