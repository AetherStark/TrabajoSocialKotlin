package com.example.myproyectofinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.*
import com.example.myproyectofinal.BaseDatos.adminBD
import com.example.myproyectofinal.Propiedades.Alumnos
import com.example.myproyectofinal.Propiedades.Reportes
import kotlinx.android.synthetic.main.activity__recycler__alumnos.*
import kotlinx.android.synthetic.main.activity__recycler__reportes.*

class Activity_Recycler_Reportes : AppCompatActivity() {

    private lateinit var viewAdapter: AdaptadorReportes
    private lateinit var viewManager: RecyclerView.LayoutManager
    val reportesList: List<Reportes> = ArrayList()
    var IDA =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        var Actividad = intent
        if (Actividad != null && Actividad.hasExtra("IDA")){
            IDA = Actividad.getStringExtra("IDA")
            //Layout.text= "Tu IMC es de: $imc"
        }


        setContentView(R.layout.activity__recycler__alumnos)


        viewManager = LinearLayoutManager(this)
        viewAdapter = AdaptadorReportes(reportesList, this, {repor: Reportes -> onItemClickListener(repor)})

        rv_alum_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(DividerItemDecoration(this@Activity_Recycler_Reportes, DividerItemDecoration.VERTICAL))
        }


        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                val repor= viewAdapter.getTasks()
                val admin = adminBD(baseContext)
                if (admin.Ejecuta("DELETE FROM Reportes WHERE idreporte=" + repor[position].idr)){
                    retrieveReportes()
                }
            }
        }).attachToRecyclerView(rv_report_list)
    }

    // Evento clic cuando damos clic en un elemento del Recyclerview
    private fun onItemClickListener(repor: Reportes) {

        //Toast.makeText(this, "Abriendo Reporte: " + repor.idr, Toast.LENGTH_LONG).show()
        var NumRepor = repor.idr
        Toast.makeText(this, "El reporte es el numero " + repor.idr, Toast.LENGTH_SHORT).show();
       // val actividad= Intent(this,ActivityMuestraAlumno::class.java)
        //actividad.putExtra("IDA",NumRepor)

        //startActivity(actividad)
    }

    override fun onResume() {
        super.onResume()
        retrieveReportes()
    }

    private fun retrieveReportes() {
        val reportx = getReportes()
        viewAdapter.setTask(reportx!!)
    }

    fun getReportes(): MutableList<Reportes>{
        var repor:MutableList<Reportes> = ArrayList()
        val admin = adminBD(this)

        //                                          0       1       2      3
        val tupla = admin.Consulta("SELECT idreporte,fecha,motivo_r,diagnostico,descripcion,seguimiento,idalumno FROM Reportes WHERE idalumno='$IDA' ORDER BY fecha")
        while (tupla!!.moveToNext()) {
            val idr = tupla.getString(0)
            val fecha = tupla.getString(1)
            val motivo = tupla.getString(2)
            val diag = tupla.getString(3)
            val descrip = tupla.getString(4)
            val segui = tupla.getString(5)
            val ida = tupla.getString(6)

            repor.add(Reportes(idr,fecha,motivo,diag,descrip,segui,ida))
        }
        tupla.close()
        admin.close()
        return repor
    }
}
