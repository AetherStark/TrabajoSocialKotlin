package com.example.myproyectofinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.widget.Toast
import androidx.recyclerview.widget.*
import com.example.myproyectofinal.BaseDatos.adminBD
import com.example.myproyectofinal.Propiedades.Alumnos
import com.example.myproyectofinal.Propiedades.Escuelas
import kotlinx.android.synthetic.main.activity__recycler__alumnos.*
import kotlinx.android.synthetic.main.activity_recycler_escuelas.*

class Activity_Recycler_Alumnos : AppCompatActivity() {

    private lateinit var viewAdapter: AdaptadorAlumnos
    private lateinit var viewManager: RecyclerView.LayoutManager
    val alumnosList: List<Alumnos> = ArrayList()
    var IDE =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        var Actividad = intent
        if (Actividad != null && Actividad.hasExtra("IDE")){
            IDE = Actividad.getStringExtra("IDE")
            //Layout.text= "Tu IMC es de: $imc"
        }


        setContentView(R.layout.activity__recycler__alumnos)


        viewManager = GridLayoutManager(this,3)
        viewAdapter = AdaptadorAlumnos(alumnosList, this, {alum: Alumnos -> onItemClickListener(alum)})

        rv_alum_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(DividerItemDecoration(this@Activity_Recycler_Alumnos, DividerItemDecoration.VERTICAL))
        }


        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                val alum= viewAdapter.getTasks()
                val admin = adminBD(baseContext)
                if (admin.Ejecuta("DELETE FROM Alumnos WHERE idalumno=" + alum[position].ide)){
                    retrieveAlumnos()
                }
            }
        }).attachToRecyclerView(rv_alum_list)
    }

    // Evento clic cuando damos clic en un elemento del Recyclerview
    private fun onItemClickListener(alum: Alumnos) {

        Toast.makeText(this, "Abriendo Alumno: " + alum.nomA, Toast.LENGTH_LONG).show()
        var NumEscuela = alum.ida
        // Toast.makeText(this, "LA escuela es la numero " + NumEscuela, Toast.LENGTH_SHORT).show();
        val actividad= Intent(this,Borrame::class.java)
        startActivity(actividad)
    }

    override fun onResume() {
        super.onResume()
        retrieveAlumnos()
    }

    private fun retrieveAlumnos() {
        val alumnox = getAlumnos()
        viewAdapter.setTask(alumnox!!)
    }

    fun getAlumnos(): MutableList<Alumnos>{
        var alum:MutableList<Alumnos> = ArrayList()
        val admin = adminBD(this)

        //                                          0       1       2      3
        val tupla = admin.Consulta("SELECT idalumno,nombre,edad,grado,idescuela FROM Alumnos WHERE idescuela='$IDE' ORDER BY nombre")
        while (tupla!!.moveToNext()) {
            val ida = tupla.getString(0)
            val nom = tupla.getString(1)
            val eda = tupla.getString(2)
            val grad = tupla.getString(3)
            val ide = tupla.getString(4)

            alum.add(Alumnos(ida,nom,eda,grad,ide))
        }
        tupla.close()
        admin.close()
        return alum
    }
}
