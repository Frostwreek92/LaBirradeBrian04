package org.example

import org.example.Codes.funcionesExtra
import org.example.Codes.variables
import java.lang.Exception
import org.example.Codes.funcionesCervezas

class Iniciar {
    fun iniciarPrograma() {
        println(variables.titulo)
        try {
            while (!variables.salirMenuInicial) {
                val opcion = funcionesExtra.leerDato(variables.menuInicial, Int::class.java, 0)
                when (opcion) {
                    1 -> imprimirBaseDeDatos()
                    2 -> insertarInformacionBD()
                    3 -> modificarInformacionPorID()
                    4 -> eliminarDatoPorID()
                    0 -> variables.salirMenuInicial = funcionesExtra.finEleccion()
                    else -> println("Introduce un número entre 0 y 4")
                }
            }
            println(variables.finPrograma)
        } catch (e: Exception) {
            println("Excepción: $e")
        }
    }
}
val iniciar = Iniciar()

fun imprimirBaseDeDatos() {
    funcionesCervezas.imprimirCervezas()
}
fun insertarInformacionBD() {
    funcionesCervezas.insertarCervezaBD()
}
fun modificarInformacionPorID() {
    funcionesCervezas.actualizarCervezaPorID()
}
fun eliminarDatoPorID() {
    funcionesCervezas.eliminarCervezaPorID()
}