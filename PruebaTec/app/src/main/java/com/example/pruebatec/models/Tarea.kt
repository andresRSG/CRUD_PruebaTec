package com.example.pruebatec.models

import java.io.Serializable

data class Tarea(
    var idTaks: String = "",
    var description: String = "",
    var status: Int = 0, // 0: en pausa, 1: iniciada, 2: pausada, 3: en revisión, 4: Finalizada (Kanban)
    var idUser: String = "",
    var idProject: String = ""
) : Serializable