package com.example.pruebatec.aplication.crud.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.pruebatec.aplication.crud.ui.fragments.StatusDialogFragment
import com.example.pruebatec.databinding.ActivityDetailTaksBinding
import com.example.pruebatec.models.Tarea
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DetailTaksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailTaksBinding
    private  var tarea : Tarea? = null
    var db : FirebaseFirestore = Firebase.firestore
    val TAG_CRUD = "CRUD"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityDetailTaksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tarea = intent.getSerializableExtra("task") as? Tarea

        Log.v("TAREA" , "Tarea id: ${tarea?.idTaks}")
        binding.tvIdTask.text = tarea?.idTaks
        binding.tvDescription.text = tarea?.description
        Log.v("TAREA" , "Tarea id details: ${tarea?.description}")
        getConvertStatus(tarea?.status)

        listeners()

    }

    private fun getConvertStatus(status:Int?){
        if(status == null){
            binding.etStatus.setText("No iniciada")
            binding.etStatus.setTextColor(android.graphics.Color.GRAY)
        }

        val (textoEstado, colorEstado) = when (status) {
            0 -> "En pausa" to android.graphics.Color.GRAY
            1 -> "Iniciada" to android.graphics.Color.BLUE
            2 -> "Pausada" to android.graphics.Color.BLACK
            3 -> "En revisión" to android.graphics.Color.MAGENTA
            4 -> "Finalizada" to android.graphics.Color.GREEN
            else -> "Estado desconocido" to android.graphics.Color.DKGRAY
        }

        // Asignamos a tu EditText o TextView
        binding.etStatus.setText(textoEstado)
        binding.etStatus.setTextColor(colorEstado)
    }

    private fun listeners(){
        binding.etStatus.setOnClickListener {
            val dialog = StatusDialogFragment { statusCode, statusText ->
                //binding.etStatus.setText(statusText)
                tarea?.status = statusCode
                getConvertStatus(statusCode)

            }

            dialog.show(supportFragmentManager, "StatusDialog")
        }

        binding.tvBtnSave.setOnClickListener {
            binding.contraintLoader.visibility = View.VISIBLE

            val updatedTask = hashMapOf(
                "status" to tarea!!.status
            )

            db.collection("TASK")
                .document(tarea!!.idTaks)
                .update(updatedTask as Map<String, Any>)
                .addOnSuccessListener {
                    binding.contraintLoader.visibility = View.GONE
                    showToast("Tarea actualizada correctamente")
                    Log.d(TAG_CRUD, "Tarea actualizada correctamente!!")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG_CRUD, "Error al actualizar la tarea: ", e)
                    showToast("Error al actualizar la tarea")
                    binding.contraintLoader.visibility = View.GONE

                }

        }


        binding.tvBtnDelete.setOnClickListener {
            showDeleteConfirmationDialog {
                binding.contraintLoader.visibility = View.VISIBLE
                db.collection("TASK")
                    .document(tarea!!.idTaks)
                    .delete()
                    .addOnSuccessListener {
                        binding.contraintLoader.visibility = View.GONE
                        Log.d(TAG_CRUD, "Tarea eliminada")
                        super.onBackPressed()
                    }
                    .addOnFailureListener { e ->
                        binding.contraintLoader.visibility = View.GONE
                        showToast("La tarea no pudo ser eliminada")
                    }
            }
        }

        binding.vwLoader.setOnClickListener {  }



    }
    private fun showToast(message:String){

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun showDeleteConfirmationDialog(onConfirmDelete: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar tarea")
            .setMessage("¿Estás seguro de que deseas eliminar esta tarea?")
            .setPositiveButton("Sí") { dialog, _ ->
                onConfirmDelete()
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

}