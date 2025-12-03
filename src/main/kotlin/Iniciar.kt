package org.example

import org.example.Codes.funcionesExtra
import org.example.Codes.variables
import java.lang.Exception
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import org.bson.Document
import org.example.Codes.funcionesCervezas

// Configuración de conexión
const val DATABASE = "LaBirradeBrian"
const val COLECCION = "cervezas"
const val USER = "user"
const val PASSWORD = "user"
const val HOST = "localhost"
const val PORT = 27017
const val URL = "mongodb://$USER:$PASSWORD@$HOST:$PORT/$DATABASE"

class Iniciar {
    fun iniciarPrograma() {
        val client = MongoClients.create(URL)
        val database = client.getDatabase(DATABASE)
        val collection: MongoCollection<Document> = database.getCollection(COLECCION)

        println(variables.titulo)
        try {
            while (!variables.salirMenuInicial) {
                val opcion = funcionesExtra.leerDato(variables.menuInicial, Int::class.java, 0)
                when (opcion) {
                    1 -> funcionesCervezas.imprimirCervezas(collection)
                    2 -> funcionesCervezas.insertarCerveza(collection)
                    3 -> funcionesCervezas.actualizarCerveza(collection)
                    4 -> funcionesCervezas.eliminarCerveza(collection)
                    0 -> variables.salirMenuInicial = funcionesExtra.finEleccion()
                    else -> println("Introduce una de las opciones")
                }
            }
            println(variables.finPrograma)
        } catch (e: Exception) {
            println("Excepción: $e")
        }
        client.close()
    }
}
val iniciar = Iniciar()
