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
import com.example.myproyectofinal.BaseDatos.adminBD
import com.example.myproyectofinal.Volley.VolleySingleton
import kotlinx.android.synthetic.main.activity__reportes.*
import kotlinx.android.synthetic.main.activity_registro.*
import org.json.JSONObject

class Activity_Reportes : AppCompatActivity() {
    var IDA =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__reportes)
        etFecha.requestFocus()

        var Actividad = intent
        if (Actividad != null && Actividad.hasExtra("IDA")){
            IDA = Actividad.getStringExtra("IDA")
            //Layout.text= "Tu IMC es de: $imc"
        }

    }


    fun insertaReporte(v: View){

        if (etFecha.text.toString().isEmpty()||etMotivo.text.toString().isEmpty()|| etDiag.text.toString().isEmpty()||etDescrip.text.toString().isEmpty()
            ||etSegui.text.toString().isEmpty()){
            Toast.makeText(this, "Falta Informacion de Capturar", Toast.LENGTH_LONG).show();
            // etIdprod.requestFocus()
        }
        else{

                var jsonEntrada = JSONObject()
                jsonEntrada.put("fecha", etFecha.text.toString())
                jsonEntrada.put("motivo_r", etMotivo.text.toString())
                jsonEntrada.put("diagnostico", etDiag.text.toString())
                jsonEntrada.put("descripcion", etDescrip.text.toString())
                jsonEntrada.put("seguimiento", etSegui.text.toString())
                jsonEntrada.put("idalumno", IDA)
                sendRequest(Adress.IP + "WSProyFinal/InsertarReportes.php", jsonEntrada)

        }
    }

    fun sendRequest(wsURL: String,jsonEntrada: JSONObject){
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, wsURL, jsonEntrada,
            Response.Listener { response ->
                val succ = response["success"]
                val msg = response["message"]
                Toast.makeText(this, "Success:${succ}  Message:${msg}", Toast.LENGTH_LONG).show();
                getReportesWS()

                finish()

            },
            Response.ErrorListener { error ->

                Toast.makeText(this, "${error.message}", Toast.LENGTH_LONG).show();
                Log.d("ERROR","${error.message}")
                Toast.makeText(this, "API: Error de capa 8 en WS ):", Toast.LENGTH_SHORT).show();
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
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
                    Toast.makeText(this, "Informacion Cargada: "+ res, Toast.LENGTH_LONG).show();


                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error getReportes: ${error.message}", Toast.LENGTH_LONG).show();
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }
}
