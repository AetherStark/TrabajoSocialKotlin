package com.example.myproyectofinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonObjectRequest
import com.example.myproyectofinal.BaseDatos.adminBD
import com.example.myproyectofinal.Propiedades.Escuelas
import com.example.myproyectofinal.Volley.VolleySingleton
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewAdapter: AdaptadorEscuelas
    private lateinit var viewManager: RecyclerView.LayoutManager
    val escuelasList: List<Escuelas> = ArrayList()

    //agrregue comentario
    


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(this)
        viewAdapter = AdaptadorEscuelas(escuelasList, this, { escuel: Escuelas -> onItemClickListener(escuel)})

        rv_escu_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
        }


        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                val estud= viewAdapter.getTasks()
                val admin = adminBD(baseContext)
                if (admin.Ejecuta("DELETE FROM Estudiante WHERE noControl=" + estud[position].noCtrl) == 1){
                    retrieveEstudiantes()
                }
            }
        }).attachToRecyclerView(rv_escu_list)
    }

    // Evento clic cuando damos clic en un elemento del Recyclerview
    private fun onItemClickListener(Estud: estudiante) {
        Toast.makeText(this, "Clicked item" + Estud.nomEst, Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        retrieveEstudiantes()
    }

    private fun retrieveEstudiantes() {
        val estudiantex = getEstudiantes()
        viewAdapter.setTask(estudiantex!!)
    }

    fun getEstudiantes(): MutableList<estudiante>{
        var estudiantes:MutableList<estudiante> = ArrayList()
        val admin = adminBD(this)

        //                                          0       1       2      3
        val tupla = admin.Consulta("SELECT noControl,nomEst,carrera,edadEst FROM Estudiante ORDER BY nomEst")
        while (tupla!!.moveToNext()) {
            val no = tupla.getString(0)
            val nom = tupla.getString(1)
            val car = tupla.getString(2)
            val eda = tupla.getInt(3)

            estudiantes.add(estudiante(no,nom,car,eda))
        }
        tupla.close()
        admin.close()
        return estudiantes





    getAllTrabajadores()

        val actividad= Intent(this,ActivityLogin::class.java)
        startActivity(actividad)
    }


    fun getAllTrabajadores(){
        val wsURL = Adress.IP +"WSProyFinal/getTrabajadores.php"
        val admin = adminBD(this)
        admin.Ejecuta("DELETE FROM Trabajadores")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,wsURL,null,
            Response.Listener { response ->
                val succ= response["success"]
                val msg = response["message"]
                val trabajadoresJson = response.getJSONArray("trabajadores")
                for(i in 0 until trabajadoresJson.length()){
                    val idt = trabajadoresJson.getJSONObject(i).getString("idtrabajador")
                    val nom = trabajadoresJson.getJSONObject(i).getString("nombre")
                    val corr = trabajadoresJson.getJSONObject(i).getString("correo")
                    val dom = trabajadoresJson.getJSONObject(i).getString("domicilio")
                    val tel = trabajadoresJson.getJSONObject(i).getString("telefono")
                    val pass = trabajadoresJson.getJSONObject(i).getString("password")
                    val sentencia = "Insert into Trabajadores(idtrabajador,nombre,correo,domicilio,telefono,password) values(${idt},'${nom}', '${corr}','${dom}','${tel}','${pass}')"
                    var res =admin.Ejecuta(sentencia)
                    Toast.makeText(this, "Informacion Cargada: "+ res, Toast.LENGTH_LONG).show();


                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error getTrabajadores: ${error.message}", Toast.LENGTH_LONG).show();
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }



}
