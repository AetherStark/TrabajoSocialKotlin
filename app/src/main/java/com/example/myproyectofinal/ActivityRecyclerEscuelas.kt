package com.example.myproyectofinal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myproyectofinal.BaseDatos.adminBD
import com.example.myproyectofinal.Propiedades.Escuelas
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_recycler_escuelas.*

class ActivityRecyclerEscuelas : AppCompatActivity() {

    private lateinit var viewAdapter: AdaptadorEscuelas
    private lateinit var viewManager: RecyclerView.LayoutManager
    val escuelasList: List<Escuelas> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_escuelas)


        viewManager = LinearLayoutManager(this)
        viewAdapter = AdaptadorEscuelas(escuelasList, this, { escuel: Escuelas -> onItemClickListener(escuel)})

        rv_escu_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(DividerItemDecoration(this@ActivityRecyclerEscuelas, DividerItemDecoration.VERTICAL))
        }


        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                val estud= viewAdapter.getTasks()
                val admin = adminBD(baseContext)
                if (admin.Ejecuta("DELETE FROM Escuela WHERE idescuela=" + estud[position].ide)){
                    retrieveEstudiantes()
                }
            }
        }).attachToRecyclerView(rv_escu_list)
    }

    // Evento clic cuando damos clic en un elemento del Recyclerview
    private fun onItemClickListener(Escuel: Escuelas) {
        Toast.makeText(this, "Clicked item" + Escuel.nomE, Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        retrieveEstudiantes()
    }

    private fun retrieveEstudiantes() {
        val estudiantex = getEstudiantes()
        viewAdapter.setTask(estudiantex!!)
    }

    fun getEstudiantes(): MutableList<Escuelas>{
        var escuel:MutableList<Escuelas> = ArrayList()
        val admin = adminBD(this)

        //                                          0       1       2      3
        val tupla = admin.Consulta("SELECT idescuela,nombre,direccion,telefono,idtrabajador FROM Escuela ORDER BY nombre")
        while (tupla!!.moveToNext()) {
            val ide = tupla.getString(0)
            val nom = tupla.getString(1)
            val dir = tupla.getString(2)
            val tel = tupla.getString(3)
            val idt = tupla.getString(4)

            escuel.add(Escuelas(ide,nom,dir,tel,idt))
        }
        tupla.close()
        admin.close()
        return escuel
    }
}
