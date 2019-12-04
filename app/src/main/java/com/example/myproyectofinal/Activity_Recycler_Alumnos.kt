package com.example.myproyectofinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.widget.Toast
import androidx.recyclerview.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.myproyectofinal.BaseDatos.adminBD
import com.example.myproyectofinal.Propiedades.Alumnos
import com.example.myproyectofinal.Propiedades.Escuelas
import com.example.myproyectofinal.Volley.VolleySingleton
import kotlinx.android.synthetic.main.activity__recycler__alumnos.*
import kotlinx.android.synthetic.main.activity_recycler_escuelas.*

class Activity_Recycler_Alumnos : AppCompatActivity() {

    private lateinit var viewAdapter: AdaptadorAlumnos
    private lateinit var viewManager: RecyclerView.LayoutManager
    val alumnosList: List<Alumnos> = ArrayList()
    var IDE =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getReportesWS()

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

        Toast.makeText(this, "Abriendo Alumno: " + alum.nomA, Toast.LENGTH_SHORT).show()
        var NumAlumno = alum.ida
        //Toast.makeText(this, "LA escuela es la numero " + NumAlumno, Toast.LENGTH_SHORT).show();
        val actividad= Intent(this,ActivityMuestraAlumno::class.java)
        actividad.putExtra("IDA",NumAlumno)

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

    fun getReportesWS(){
        val wsURL = Adress.IP +"WSProyFinal/getReportes.php"
        val admin = adminBD(this)
        admin.Ejecuta("DELETE FROM Reportes")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,wsURL,null,
            Response.Listener { response ->
                val succ= response["success"]
                val msg = response["message"]
                val trabajadoresJson = response.getJSONArray("reportes")
                for(i in 0 until trabajadoresJson.length()){
                    val idr = trabajadoresJson.getJSONObject(i).getString("idreporte")
                    val fech = trabajadoresJson.getJSONObject(i).getString("fecha")
                    val motivo = trabajadoresJson.getJSONObject(i).getString("motivo_r")
                    val diag = trabajadoresJson.getJSONObject(i).getString("diagnostico")
                    val descrip = trabajadoresJson.getJSONObject(i).getString("descripcion")
                    val segui = trabajadoresJson.getJSONObject(i).getString("seguimiento")
                    val ida = trabajadoresJson.getJSONObject(i).getString("idalumno")
                    val sentencia = "Insert into Reportes(idreporte,fecha,motivo_r,diagnostico,descripcion,seguimiento,idalumno)" +
                            "values(${idr},'${fech}','${motivo}','${diag}','${descrip}','${segui}','${ida}')"
                    var res =admin.Ejecuta(sentencia)
                      // Toast.makeText(this, "Informacion Cargada: "+ res, Toast.LENGTH_LONG).show();


                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error getReportes: ${error.message}", Toast.LENGTH_LONG).show();
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }
}
