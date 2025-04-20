package com.example.pruebatec.aplication.crud.ui.fragments

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pruebatec.R
import com.example.pruebatec.aplication.crud.ui.OnTaskClickListener
import com.example.pruebatec.models.Tarea

class AdapterTaks (private val listTaks: List<Tarea>, private val listener: OnTaskClickListener) : RecyclerView.Adapter<AdapterTaks.MyViewHolder>() {

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val tvID : TextView = itemView.findViewById(R.id.tv_taskID)
        val ivCheckDetails : ImageView = itemView.findViewById(R.id.iv_checkDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_taks_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = listTaks.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val itemTask = listTaks[position]
        holder.tvID.text = "${itemTask.idTaks}"
        holder.ivCheckDetails.setOnClickListener{
            Log.v("CRUD", "Tarea con el ID: ${itemTask.idTaks}")
            listener.onTaskDetailClick(itemTask)
            
        }
    }

}