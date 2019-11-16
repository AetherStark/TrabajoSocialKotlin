package com.example.myproyectofinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myproyectofinal.BaseDatos.adminBD
import com.example.myproyectofinal.Propiedades.Alumnos
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_muestra_alumno.*

class ActivityMuestraAlumno : AppCompatActivity() {

    var IDA=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_muestra_alumno)

        var Actividad = intent
        if (Actividad != null && Actividad.hasExtra("IDA")){
            IDA = Actividad.getStringExtra("IDA")
            //Layout.text= "Tu IMC es de: $imc"
        }

        fab1.setOnClickListener { view ->

            val actividad= Intent(this,Borrame::class.java)
            startActivity(actividad)
           // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
             //   .setAction("Action", null).show()
        }
        fab2.setOnClickListener { view ->
            val actividad= Intent(this,Borrame::class.java)
            startActivity(actividad)
           // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
             //   .setAction("Action", null).show()
        }

        getAlumnos()
    }


    fun getAlumnos(): MutableList<Alumnos>{
        var alum:MutableList<Alumnos> = ArrayList()
        val admin = adminBD(this)

        //                                          0       1       2      3
        val tupla = admin.Consulta("SELECT idalumno,nombre,edad,grado,idescuela FROM Alumnos WHERE idalumno='$IDA' ORDER BY nombre")
        while (tupla!!.moveToNext()) {
            val ida = tupla.getString(0)
            val nom = tupla.getString(1)
            val eda = tupla.getString(2)
            val grad = tupla.getString(3)
            val ide = tupla.getString(4)

            tvNombre.text=nom.toString()
            tvEdad.text=eda.toString() +" Años"
            tvGrado.text=grad.toString() +" Grado"
        }
        tupla.close()
        admin.close()
        return alum
    }
}
