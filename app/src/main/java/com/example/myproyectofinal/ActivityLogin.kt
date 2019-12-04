package com.example.myproyectofinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.myproyectofinal.BaseDatos.adminBD
import com.example.myproyectofinal.Volley.VolleySingleton
import kotlinx.android.synthetic.main.activity_login.*

class ActivityLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        getEscuelas()

    }
    fun Cambio(v:View){

        val actividad= Intent(this,ActivityRegistro::class.java)
        startActivity(actividad)
    }

    fun comprobar(v:View){

        var query:String
        var correo2=""
        var contra1=etPassword.text.toString()
        var contra2=""


        if (etCorreo.text.isEmpty()||etPassword.text.isEmpty()){
            Toast.makeText(this, "FALTAN DATOS DE ENTRADA", Toast.LENGTH_LONG ).show();
            etCorreo.requestFocus()
        }
        else{
            val correo= etCorreo.text.toString()
            query= "Select idtrabajador,nombre,correo,password from Trabajadores where correo='$correo'"
            var admin = adminBD(this)
            var cur= admin.Consulta(query)


            if (cur== null){
                Toast.makeText(this, "Error de Capa 8", Toast.LENGTH_LONG).show();
                etCorreo.requestFocus()

            }
            else{
                if( cur.moveToFirst()) {
                    val trabajador =cur.getString(1)
                    correo2=cur.getString(2)
                    contra2=cur.getString(3)
                    if (correo==correo2&&contra1==contra2){

                        val actividad= Intent(this,ActivityRecyclerEscuelas::class.java)
                        Toast.makeText(this, "Bienvenido "+trabajador, Toast.LENGTH_SHORT).show();
                        startActivity(actividad)
                    }else{
                        Toast.makeText(this, "DATOS INCORRECTOS", Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    Toast.makeText(this, "No se Encontro el Teabajador", Toast.LENGTH_LONG).show();
                    etCorreo.requestFocus()
                }
            }

        }


    }


    fun getEscuelas(){
        val wsURL = Adress.IP +"WSProyFinal/getEscuelas.php"
        val admin = adminBD(this)
        admin.Ejecuta("DELETE FROM Escuela")

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,wsURL,null,
            Response.Listener { response ->
                val succ= response["success"]
                val msg = response["message"]
                val escuelasJson = response.getJSONArray("escuelas")
                for(i in 0 until escuelasJson.length()){
                    val ide = escuelasJson.getJSONObject(i).getString("idescuela")
                    val nom = escuelasJson.getJSONObject(i).getString("nombre")
                    val dir = escuelasJson.getJSONObject(i).getString("direccion")
                    val tel = escuelasJson.getJSONObject(i).getString("telefono")
                    val idt = escuelasJson.getJSONObject(i).getString("idtrabajador")
                    val sentencia = "Insert into Escuela(idescuela,nombre,direccion,telefono,idtrabajador) values(${ide},'${nom}', '${dir}','${tel}','${idt}')"
                    var res =admin.Ejecuta(sentencia)
                   // Toast.makeText(this, "Escuelas Cargadas: "+ res, Toast.LENGTH_LONG).show();


                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error getEscuelas: ${error.message}", Toast.LENGTH_LONG).show();
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

}

