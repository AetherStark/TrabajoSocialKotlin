package com.example.myproyectofinal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myproyectofinal.Propiedades.Alumnos
import com.example.myproyectofinal.Propiedades.Escuelas
import kotlinx.android.synthetic.main.celda_prototipo_alumnos.view.*
import kotlinx.android.synthetic.main.celda_prototipo_escuelas.view.*

class AdaptadorAlumnos (private var mListaAlumnos:List<Alumnos>,
                         private val mContext: Context, private val clickListener: (Alumnos) -> Unit)

    : RecyclerView.Adapter<AdaptadorAlumnos.AlumnosViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlumnosViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        return AlumnosViewHolder(
            layoutInflater.inflate(
                R.layout.celda_prototipo_alumnos,
                parent,
                false
            )
        )
    }

    /**
     * La clase RecyclerView. onBindViewHolder() se encarga de coger cada una de las
     * posiciones de la lista de estudiantes y pasarlas a la clase ViewHolder(
     *
     * @param holder   Vincular los datos del cursor al ViewHolder
     * @param position La posición de los datos en la lista
     */
    override fun onBindViewHolder(holder: AlumnosViewHolder, position: Int) {
        holder.bind(mListaAlumnos[position], mContext, clickListener)


    }

    /**
     * El método getItemCount() nos devuelve el tamaño de la lista, que lo necesita
     * el RecyclerView.
     */
    override fun getItemCount(): Int = mListaAlumnos.size

    /**
     * Cuando los datos cambian, este metodo actualiza la lista de estudiantes
     * y notifica al adaptador a usar estos nuevos valores
     */
    fun setTask(alumnos: List<Alumnos>) {
        mListaAlumnos = alumnos
        notifyDataSetChanged()
    }

    fun getTasks(): List<Alumnos> = mListaAlumnos

    /**
     * Clase interna para crear ViewHolders
     */
    class AlumnosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(alumnos: Alumnos, context: Context, clickListener: (Alumnos) -> Unit) {
            //Asigna los valores a los elementos del la celda_prototipo_estudiante
            itemView.NombA.text = alumnos.nomA.toString()
            itemView.Grado.text = alumnos.grad.toString()+ " Semestre"

            itemView.setOnClickListener { clickListener(alumnos) }
        }
    }
}