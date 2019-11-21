package com.example.myproyectofinal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myproyectofinal.Propiedades.Alumnos
import com.example.myproyectofinal.Propiedades.Reportes
import kotlinx.android.synthetic.main.celda_prototipo_alumnos.view.*
import kotlinx.android.synthetic.main.celda_prototipo_reportes.view.*

class AdaptadorReportes (private var mListaReportes:List<Reportes>,
                         private val mContext: Context, private val clickListener: (Reportes) -> Unit)

    : RecyclerView.Adapter<AdaptadorReportes.ReportesViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportesViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        return ReportesViewHolder(
            layoutInflater.inflate(
                R.layout.celda_prototipo_reportes,
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
    override fun onBindViewHolder(holder: ReportesViewHolder, position: Int) {
        holder.bind(mListaReportes[position], mContext, clickListener)


    }

    /**
     * El método getItemCount() nos devuelve el tamaño de la lista, que lo necesita
     * el RecyclerView.
     */
    override fun getItemCount(): Int = mListaReportes.size

    /**
     * Cuando los datos cambian, este metodo actualiza la lista de estudiantes
     * y notifica al adaptador a usar estos nuevos valores
     */
    fun setTask(repor: List<Reportes>) {
        mListaReportes = repor
        notifyDataSetChanged()
    }

    fun getTasks(): List<Reportes> = mListaReportes
    /**
     * Clase interna para crear ViewHolders
     */
    class ReportesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(repor: Reportes, context: Context, clickListener: (Reportes) -> Unit) {
            //Asigna los valores a los elementos del la celda_prototipo_estudiante
            itemView.NoReport.text = repor.idr.toString()
            itemView.FechReport.text = repor.fecha.toString()
            itemView.MotivoReport.text = repor.motivo.toString()

            itemView.setOnClickListener { clickListener(repor) }
        }
    }
}