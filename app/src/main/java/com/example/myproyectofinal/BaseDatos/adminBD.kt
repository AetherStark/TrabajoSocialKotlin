package com.example.myproyectofinal.BaseDatos

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.Exception


class adminBD(context: Context): SQLiteOpenHelper(context, DATABASE, null, 1) {
    companion object{
        val DATABASE = "TSocial"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "Create Table Alumnos(" +
                    "idalumno integer primary key AUTOINCREMENT," +
                    "nombre text," +
                    "edad text," +
                    "grado text," +
                    "idescuela int)"
        )
        db?.execSQL(
            "Create Table Trabajadores(" +
                    "idtrabajador integer primary key AUTOINCREMENT," +
                    "nombre text," +
                    "correo text," +
                    "domicilio text," +
                    "telefono int," +
                    "password text)"
        )
        db?.execSQL(
            "Create Table Escuela(" +
                    "idescuela integer primary key AUTOINCREMENT," +
                    "nombre text," +
                    "direccion text," +
                    "telefono text," +
                    "idtrabajador int)"
        )
        db?.execSQL(
            "Create Table Reportes(" +
                    "idreporte integer primary key AUTOINCREMENT," +
                    "fecha text," +
                    "cuestionario text," +
                    "idalumno text," +
                    "idtrabajador integer)"
        )
    }

    //Funcion para mandar ejecutar un INSERT, UPDATE o DELETE
    fun Ejecuta(sentencia: String): Boolean{
        try {
            val db = this.writableDatabase
            db.execSQL(sentencia)
            db.close()
            return true
        }
        catch(ex: Exception){
            return false

        }
    }


    fun Consulta(query: String): Cursor?{

        try {
            val db = this.readableDatabase
            return db.rawQuery(query, null)
        }
        catch(ex: Exception){
            return null
        }
    }



    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}