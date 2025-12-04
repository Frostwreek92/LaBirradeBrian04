package org.example

import java.lang.Exception
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document
import org.example.Codes.variables
import org.example.Codes.funcionesCervezas
import org.example.Codes.funcionesExtra
import org.bson.json.JsonWriterSettings
import org.example.Codes.COLECCION
import org.example.Codes.DATABASE
import org.example.Codes.URL
import org.json.JSONArray
import java.io.File

class Iniciar {
    fun iniciarPrograma() {
        val client = MongoClients.create(URL)
        val database = client.getDatabase(DATABASE)
        val collection: MongoCollection<Document> = database.getCollection(COLECCION)

        println(variables.titulo)
        try {
            while (!variables.salirMenuInicial) {
                val opcion = funcionesExtra.leerDato(variables.menuInicial, Int::class.java)
                when (opcion) {
                    1 -> menuCRUD(collection)
                    2 -> consultasAdicionales(collection)
                    3 -> exportarBD(collection, variables.rutaResourcesJSON)
                    4 -> importarBD(variables.rutaResourcesJSON, database, COLECCION)
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

fun menuCRUD(collection: MongoCollection<Document>) {
    variables.salirMenuCRUD = false
    try {
        while (!variables.salirMenuCRUD) {
            val opcion = funcionesExtra.leerDato(variables.menuCRUD, Int::class.java)
            when (opcion) {
                1 -> funcionesCervezas.imprimirCervezas(collection)
                2 -> funcionesCervezas.insertarCerveza(collection)
                3 -> funcionesCervezas.actualizarCerveza(collection)
                4 -> funcionesCervezas.eliminarCerveza(collection)
                0 -> variables.salirMenuCRUD = funcionesExtra.finEleccion()
            }
        }
    } catch (e: Exception) {
        println("Excepción: $e")
    }
}
fun consultasAdicionales(collection: MongoCollection<Document>) {
    variables.salirConsultasAdicionales = false
    try {
        while (!variables.salirConsultasAdicionales) {
            val opcion = funcionesExtra.leerDato(variables.menuConsultasAdicionales, Int::class.java)
            when (opcion) {
                1 -> funcionesCervezas.consultarConFiltros(collection)
                2 -> funcionesCervezas.consultarConProyeccion(collection)
                3 -> funcionesCervezas.calcularPromedioGraduacion(collection)
                4 -> funcionesCervezas.promedioPuntuacionPorTipo(collection)
                5 -> funcionesCervezas.top3Cervezas(collection)
                0 -> variables.salirConsultasAdicionales = funcionesExtra.finEleccion()
            }
        }
    } catch (e: Exception) {
        println("Excepción: $e")
    }
}
fun exportarBD(coleccion: MongoCollection<Document>, rutaJSON: String) {
    val settings = JsonWriterSettings.builder().indent(true).build()
    val file = File(rutaJSON)
    file.printWriter().use { out ->
        out.println("[")
        val cursor = coleccion.find().iterator()
        var first = true
        while (cursor.hasNext()) {
            if (!first) out.println(",")
            val doc = cursor.next()
            out.print(doc.toJson(settings))
            first = false
        }
        out.println("]")
        cursor.close()
    }

    println("Exportación de ${coleccion.namespace.collectionName} completada")
}
fun importarBD(rutaJSON: String, db: MongoDatabase, nombreColeccion: String) {
    println("Iniciando importación de datos desde JSON...")

    val jsonFile = File(rutaJSON)
    if (!jsonFile.exists()) {
        println("No se encontró el archivo JSON a importar")
        return
    }
    val jsonText = try {
        jsonFile.readText()
    } catch (e: Exception) {
        println("Error leyendo el archivo JSON: ${e.message}")
        return
    }
    val array = try {
        JSONArray(jsonText)
    } catch (e: Exception) {
        println("Error al parsear JSON: ${e.message}")
        return
    }
    val documentos = mutableListOf<Document>()
    for (i in 0 until array.length()) {
        val doc = Document.parse(array.getJSONObject(i).toString())
        doc.remove("_id")
        documentos.add(doc)
    }
    if (documentos.isEmpty()) {
        println("El archivo JSON está vacío")
        return
    }
    if (db.listCollectionNames().contains(nombreColeccion)) {
        db.getCollection(nombreColeccion).drop()
        println("Colección '$nombreColeccion' eliminada antes de importar.")
    }
    try {
        val coleccion: MongoCollection<Document> = db.getCollection(nombreColeccion)
        coleccion.insertMany(documentos)
        println("Importación completada: ${documentos.size} documentos en $nombreColeccion.")
    } catch (e: Exception) {
        println("Error importando documentos: ${e.message}")
    }
}