package com.example.myproyectofinal.Propiedades

class Reportes (idreporte: String, fecha: String, motivo_r: String, diagnostico: String, descripcion: String, seguimiento: String, idalumno: String) {
    var idr: String = ""
    var fecha: String=""
    var motivo: String =""
    var diag: String = ""
    var descrip: String = ""
    var segui: String = ""
    var ida: String =""

    init{
        this.idr = idreporte
        this.fecha = fecha
        this.motivo = motivo_r
        this.diag = diagnostico
        this.descrip = descripcion
        this.segui = seguimiento
        this.ida = idalumno
    }
}