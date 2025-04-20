package com.example.pruebatec.aplication.crud.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pruebatec.aplication.crud.ui.fragments.AdapterTaks
import com.example.pruebatec.aplication.crud.ui.fragments.NewTaskDialogFragment
import com.example.pruebatec.databinding.ActivityBoardTaksBinding
import com.example.pruebatec.models.Tarea
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class BoardTaksActivity : AppCompatActivity(), OnTaskClickListener {

    lateinit var binding:ActivityBoardTaksBinding
    var db : FirebaseFirestore = Firebase.firestore
    val TAG_CRUD = "CRUD"
    var listTaks = mutableListOf<Tarea>()

    private lateinit var adapterTask : AdapterTaks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityBoardTaksBinding.inflate(layoutInflater)
        setContentView(binding.root)


        listeners()

    }

    private fun listeners(){
        binding.ivBtnAdd.setOnClickListener {
            Log.v("Listener", "click add")
            val dialog = NewTaskDialogFragment { descripcion ->
                createTask(descripcion)
            }

            dialog.show(supportFragmentManager, "NuevaTareaDialog")
        }

        binding.vwLoader.setOnClickListener {  }

    }

    private fun createTask(description:String){
        val taksRequest = hashMapOf(
            "description" to description,
        )

        db.collection("TASK")
            .add(taksRequest)
            .addOnSuccessListener { documentReference ->
                showToast("Tarea creada con exito.")
                Log.d(TAG_CRUD, "Tarea creada con el id: ${documentReference.id}")
                getTasks()
            }
            .addOnFailureListener { e ->
                showToast("Error al crear la tarea.")
                Log.d(TAG_CRUD, "Error al crear la tarea: ${e}")
            }
        Log.d("NuevaTarea", "Descripción: $description")

    }

    override fun onResume() {
        super.onResume()
        getTasks()
    }

    private fun getTasks(){
        binding.contraintLoader.visibility = View.VISIBLE
        listTaks.clear()

        db.collection("TASK")
            .get()
            .addOnSuccessListener { result ->
                binding.contraintLoader.visibility = View.GONE
                for (document in result) {
                    val task = Tarea()
                    task.idTaks = document.id
                    task.description = document.getString("description") ?: ""
                    task.idUser = document.getString("idUser") ?: ""
                    task.status = (document.getLong("status") ?: 0L).toInt()
                    listTaks.add(task)
                    Log.d(TAG_CRUD, "Tarea: $task")

                    setTasks()

                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG_CRUD, "Error al obtener tareas", e)
                binding.contraintLoader.visibility = View.GONE
            }
    }

    private fun setTasks(){
        Log.w(TAG_CRUD, "Elementos de la lista: ${listTaks.size}")
        if(listTaks.size != 0){
            adapterTask = AdapterTaks(listTaks, this)
            binding.recyclerTasks.adapter = adapterTask
            binding.recyclerTasks.layoutManager = LinearLayoutManager(this)

        }


    }

    override fun onTaskDetailClick(tarea: Tarea) {
        val i = Intent(this, DetailTaksActivity::class.java)
        i.putExtra("task", tarea)
        Log.v("TAREA" , "Tarea id: ${tarea.idTaks}")
        Log.v("TAREA" , "Tarea description: ${tarea.description}")
        startActivity(i)

    }

    private fun showToast(message:String){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }



}