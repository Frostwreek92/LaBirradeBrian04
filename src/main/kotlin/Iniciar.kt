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
                println(variables.menuInicial)
                val opcion = funcionesExtra.leerDato("Introduce una opción entre 0 y 4: ", Int::class.java, 0)
                when (opcion) {
                    1 -> imprimirBaseDeDatos()
                    2 -> println("Insertar Información")
                    3 -> println("Modificar Información")
                    4 -> println("Eliminar Información")
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