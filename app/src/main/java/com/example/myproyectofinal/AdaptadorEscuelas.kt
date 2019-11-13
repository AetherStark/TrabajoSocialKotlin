package com.example.myproyectofinal

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myproyectofinal.Propiedades.Escuelas
import kotlinx.android.synthetic.main.celda_prototipo_escuelas.view.*

class AdaptadorEscuelas (private var mListaEscuelas:List<Escuelas>,
                         private val mContext: Context, private val clickListener: (Escuelas) -> Unit)

: RecyclerView.Adapter<AdaptadorEscuelas.EscuelasViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EscuelasViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        return EscuelasViewHolder(layoutInflater.inflate(R.layout.celda_prototipo_escuelas, parent, false))
    }

    /**
     * La clase RecyclerView. onBindViewHolder() se encarga de coger cada una de las
     * posiciones de la lista de estudiantes y pasarlas a la clase ViewHolder(
     *
     * @param holder   Vincular los datos del cursor al ViewHolder
     * @param position La posición de los datos en la lista
     */
    override fun onBindViewHolder(holder: EscuelasViewHolder, position: Int) {
        holder.bind(mListaEscuelas[position], mContext, clickListener)



    }

    /**
     * El método getItemCount() nos devuelve el tamaño de la lista, que lo necesita
     * el RecyclerView.
     */
    override fun getItemCount(): Int = mListaEscuelas.size

    /**
     * Cuando los datos cambian, este metodo actualiza la lista de estudiantes
     * y notifica al adaptador a usar estos nuevos valores
     */
    fun setTask(escuelas: List<Escuelas>){
        mListaEscuelas = escuelas
        notifyDataSetChanged()
    }

    fun getTasks(): List<Escuelas> = mListaEscuelas

    /**
     * Clase interna para crear ViewHolders
     */
    class EscuelasViewHolder (itemView: View) :RecyclerView.ViewHolder(itemView) {

        fun bind (escuel:Escuelas, context: Context, clickListener: (Escuelas) -> Unit){
            //Asigna los valores a los elementos del la celda_prototipo_estudiante
            itemView.tvNomE.text = escuel.nomE.toString()
            itemView.tvTel.text = escuel.tel.toString()

            itemView.setOnClickListener{ clickListener(escuel)}
        }
    }



}