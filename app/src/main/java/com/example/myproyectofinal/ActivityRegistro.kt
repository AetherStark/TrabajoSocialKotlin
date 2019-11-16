package com.example.myproyectofinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.myproyectofinal.Volley.VolleySingleton
import kotlinx.android.synthetic.main.activity_registro.*
import org.json.JSONObject

class ActivityRegistro : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        etNombre.requestFocus()
    }

    fun insertaTrabajador(v: View){

        if (etNombre.text.toString().isEmpty()||etMail.text.toString().isEmpty()|| etPass.text.toString().isEmpty()||etPass.text.toString().isEmpty()){
            Toast.makeText(this, "Falta Informacion de Capturar", Toast.LENGTH_LONG).show();
            // etIdprod.requestFocus()
        }
        else{
            if (etPass.text.toString()==etPass2.text.toString()) {
                var jsonEntrada = JSONObject()
                jsonEntrada.put("nombre", etNombre.text.toString())
                jsonEntrada.put("correo", etMail.text.toString())
                jsonEntrada.put("password", etPass.text.toString())
                sendRequest(Adress.IP + "WSProyFinal/InsertarTrabajador.php", jsonEntrada)
            }
            else{
                Toast.makeText(this, "Las Contraseñas no coinciden", Toast.LENGTH_LONG).show();
            }
        }
    }

    fun sendRequest(wsURL: String,jsonEntrada: JSONObject){
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, wsURL, jsonEntrada,
            Response.Listener { response ->
                val succ = response["success"]
                val msg = response["message"]
                Toast.makeText(this, "Success:${succ}  Message:${msg}", Toast.LENGTH_LONG).show();
                val actividad= Intent(this,MainActivity::class.java)
                startActivity(actividad)

            },
            Response.ErrorListener { error ->

                Toast.makeText(this, "${error.message}", Toast.LENGTH_LONG).show();
               Log.d("ERROR","${error.message}")
                Toast.makeText(this, "API: Error de capa 8 en WS ):", Toast.LENGTH_SHORT).show();
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
}

