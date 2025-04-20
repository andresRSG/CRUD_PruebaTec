package com.example.pruebatec.aplication.crud.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pruebatec.R
import com.example.pruebatec.databinding.ActivityMainBinding
import com.example.pruebatec.models.Tarea
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    lateinit var  binding: ActivityMainBinding

    var db : FirebaseFirestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent:Intent = Intent(this, BoardTaksActivity::class.java)
        startActivity(intent)
        finish()
        //Interacciones con ui
        listeners()
        //crear()
        
    }
    
    private fun listeners(){
        binding.btnCrear.setOnClickListener {
            Log.d("LISTENERS", "CLICK BTN CREAR IN MAIN ACTIVITY")
            crear()
        }

        binding.btnActualizar.setOnClickListener {
            actualizar()
        }

        binding.btnCrear.setOnClickListener {
            obtener()
        }

        binding.btnCrear.setOnClickListener {
            eliminar()
        }
    }

    val TAG_CRUD = "CRUD"

    private fun crear(){
        var task = infoDummy()

        val taksRequest = hashMapOf(
            "description" to task.description,
            "idUser" to task.idUser,
            "status" to task.status,
        )

        db.collection("TASK")
            .add(taksRequest)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG_CRUD, "Tarea creada con el id: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.d(TAG_CRUD, "Error al crear la tarea: ${e}")
            }

    }

    private fun infoDummy():Tarea{
        var task = Tarea()
        task.idUser = "hdsgfk3e6dsabas"
        task.status = 0
        task.description = "Realizar pruebas"

        return task
    }
    
    private fun actualizar(){
        val updatedTask = hashMapOf(
            "description" to "Nueva descripción",
            "status" to "completado"
        )

        db.collection("TASK")
            .document("ID_DEL_DOCUMENTO")
            .update(updatedTask as Map<String, Any>)
            .addOnSuccessListener {
                Log.d(TAG_CRUD, "Tarea actualizada correctamente")
            }
            .addOnFailureListener { e ->
                Log.w(TAG_CRUD, "Error al actualizar la tarea", e)
            }
    }
    
    private fun eliminar(){
        db.collection("TASK")
            .document("ID_DEL_DOCUMENTO")
            .delete()
            .addOnSuccessListener {
                Log.d(TAG_CRUD, "Tarea eliminada")
            }
            .addOnFailureListener { e ->
                Log.w(TAG_CRUD, "Error al eliminar la tarea", e)
            }
    }
    
    private fun obtener(){
        db.collection("TASK")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val task = Tarea()
                        task.idTaks = document.id
                        task.description = document.getString("description") ?: ""
                        task.idUser = document.getString("idUser") ?: ""
                        task.status = (document.getLong("status") ?: 0) as Int

                    Log.d(TAG_CRUD, "Tarea: $task")
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG_CRUD, "Error al obtener tareas", e)
            }
    }
    
    
}