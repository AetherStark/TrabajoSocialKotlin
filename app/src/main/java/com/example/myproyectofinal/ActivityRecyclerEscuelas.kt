package com.example.myproyectofinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.myproyectofinal.BaseDatos.adminBD
import com.example.myproyectofinal.Propiedades.Escuelas
import com.example.myproyectofinal.Volley.VolleySingleton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_recycler_escuelas.*

class ActivityRecyclerEscuelas : AppCompatActivity() {

    private lateinit var viewAdapter: AdaptadorEscuelas
    private lateinit var viewManager: RecyclerView.LayoutManager
    val escuelasList: List<Escuelas> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_escuelas)
        getAlumnosWS()


        viewManager = LinearLayoutManager(this)
        viewAdapter = AdaptadorEscuelas(escuelasList, this, {escuel: Escuelas -> onItemClickListener(escuel)})

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

        Toast.makeText(this, "Abriendo Escuela: " + Escuel.nomE, Toast.LENGTH_LONG).show()
        var NumEscuela = Escuel.ide
        val actividad= Intent(this,Activity_Recycler_Alumnos::class.java)
        actividad.putExtra("IDE",NumEscuela)
       // Toast.makeText(this, "LA escuela es la numero " + NumEscuela, Toast.LENGTH_SHORT).show();

        startActivity(actividad)
    }

    override fun onResume() {
        super.onResume()
        retrieveEstudiantes()
    }

    private fun retrieveEstudiantes() {
        val estudiantex = getEscuelas()
        viewAdapter.setTask(estudiantex!!)
    }

    fun getEscuelas(): MutableList<Escuelas>{
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

    fun getAlumnosWS(){
        val wsURL = Adress.IP +"WSProyFinal/getAlumnos.php"
        val admin = adminBD(this)
        admin.Ejecuta("DELETE FROM Alumnos")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,wsURL,null,
            Response.Listener { response ->
                val succ= response["success"]
                val msg = response["message"]
                val trabajadoresJson = response.getJSONArray("alumnos")
                for(i in 0 until trabajadoresJson.length()){
                    val ida = trabajadoresJson.getJSONObject(i).getString("idalumno")
                    val nom = trabajadoresJson.getJSONObject(i).getString("nombre")
                    val eda = trabajadoresJson.getJSONObject(i).getString("edad")
                    val grad = trabajadoresJson.getJSONObject(i).getString("grado")
                    val ide = trabajadoresJson.getJSONObject(i).getString("idescuela")
                    val sentencia = "Insert into Alumnos(idalumno,nombre,edad,grado,idescuela) values(${ida},'${nom}', '${eda}','${grad}','${ide}')"
                    var res =admin.Ejecuta(sentencia)
                 //   Toast.makeText(this, "Informacion Cargada: "+ res, Toast.LENGTH_LONG).show();


                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error getAlumnos: ${error.message}", Toast.LENGTH_LONG).show();
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }
}
