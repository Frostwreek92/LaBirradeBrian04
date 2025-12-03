package org.example.Codes

class CervezasFunciones {
    data class Cerveza(
        val idCerveza: Int? = null,
        val nombreCerveza: String,
        val graduacionCerveza: Double,
        val tipoCerveza: String,
        val colorCerveza: String,
        val origenCerveza: String,
        val puntuacionCerveza: Double
    )
    object CervezasDAO {

    }
    fun imprimirCervezas() {
        println("Cervezas")
    }
}
val funcionesCervezas = CervezasFunciones()