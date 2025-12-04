package org.example.UnicoArchivo

import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Accumulators
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import com.mongodb.client.model.Sorts
import com.mongodb.client.model.Updates
import org.bson.Document
import org.bson.json.JsonWriterSettings
import org.json.JSONArray
import java.io.File
import java.lang.Exception
import kotlin.collections.contains

// Configuración de conexión
const val DATABASE = "LaBirradeBrian"
const val COLECCION = "cervezas"
const val USER = "user"
const val PASSWORD = "user"
const val HOST = "localhost"
const val PORT = 27017
const val URL = "mongodb://$USER:$PASSWORD@$HOST:$PORT/$DATABASE"

class Variables {
    // Variables de inicio
    val titulo = "La Birra de Brian: El Renacer"
    val rutaResourcesJSON = "src/main/resources/cervezas.json"
    val menuInicial = "\n" + """=== Menu principal ===
    1. CRUD
    2. Consultas Adicionales
    3. Exportar la BD
    4. Importar la BD
    0. Salir
Introduce una opción entre 0 y 4 por favor: """.trimIndent()
    val menuCRUD = "\n" + """=== Menú CRUD ===
    1. Ver Información de las Cervezas
    2. Insertar una nueva Cerveza
    3. Modificar Cerveza por ID introducido
    4. Eliminar Cerveza por ID introducido
    0. Volver
Introduce una opción entre 0 y 4 por favor:  """.trimIndent()
    val menuConsultasAdicionales = "\n" + """=== Menú Consultas Adicionales ===
    1. Consultas con Filtros
    2. Consulta con Proyecciones
    3. Calcular Promedio Graduación
    4. Promedio Puntuación por Tipo
    5. Top 3 Cervezas
    0. Volver
Introduce una opción entre 0 y 5 por favor:  """.trimIndent()
    var salirMenuInicial = false
    var salirMenuCRUD = false
    var salirConsultasAdicionales = false
    val finPrograma = "\nFin de programa"
    val menuEdicionCerveza = "\n" + """¿Qué deseas modificar?
    1. Nombre
    2. Graduación
    3. Tipo
    4. Color
    5. Origen
    6. Puntuación
    0. Cancelar
Tu opción: """.trimIndent()
}
val variables = Variables()

fun main() {
    iniciar.iniciarPrograma()
}

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
        } catch (e: java.lang.Exception) {
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
    } catch (e: java.lang.Exception) {
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
    } catch (e: java.lang.Exception) {
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
    } catch (e: java.lang.Exception) {
        println("Error leyendo el archivo JSON: ${e.message}")
        return
    }
    val array = try {
        JSONArray(jsonText)
    } catch (e: java.lang.Exception) {
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

class FuncionesCerveza {
    fun imprimirCervezas(collection: MongoCollection<Document>) {
        println("\n=== ${collection.namespace.collectionName} ===")
        for (doc in collection.find()) {
            val id = doc.getInteger("idCerveza")
            val nombre = doc.getString("nombre")
            val graduacion = doc.get("graduacion", Number::class.java).toDouble()
            val tipo = doc.getString("tipo")
            val color = doc.getString("color")
            val origen = doc.getString("origen")
            val puntuacion = doc.get("puntuacion", Number::class.java).toDouble()
            println("ID: $id - Nombre: $nombre - Graduación: $graduacion - Tipo: $tipo - Color: $color - Origen: $origen - Puntuación: $puntuacion")
        }
    }
    fun insertarCerveza(collection: MongoCollection<Document>) {
        val maxId = collection.find()
            .sort(Document("idCerveza", -1))
            .first()
            ?.getInteger("idCerveza") ?: 0  // Si no hay documentos, empieza desde 0
        val nombre = funcionesExtra.leerDato("Introduce nombre: ", String::class.java)
        val graduacion = funcionesExtra.leerDato("Introduce graducaión: ", Double::class.java)
        val tipo = funcionesExtra.leerDato("Introduce el tipo: ", String::class.java)
        val color = funcionesExtra.leerDato("Introduce el color: ", String::class.java)
        val origen = funcionesExtra.leerDato("Introduce el origen: ", String::class.java)
        val puntuacion = funcionesExtra.leerDato("Introduce la puntuación: ", Double::class.java)
        val nuevaCerveza = Document()
            .append("idCerveza", maxId + 1)
            .append("nombre", nombre)
            .append("graduacion", graduacion)
            .append("tipo", tipo)
            .append("color", color)
            .append("origen", origen)
            .append("puntuacion", puntuacion)
        collection.insertOne(nuevaCerveza)
        println("Cerveza '$nombre' añadida correctamente con ID ${maxId + 1}.")
    }
    fun actualizarCerveza(collection: MongoCollection<Document>) {
        imprimirCervezas(collection)
        val idCerveza = funcionesExtra.leerDato("Introduce ID de la cerveza a modificar: ", Int::class.java)
        // Buscar el documento
        val doc = collection.find(Document("idCerveza", idCerveza)).first()
        if (doc == null) {
            println("No existe ninguna cerveza con ID = $idCerveza")
            return
        }
        println("Cerveza seleccionada: ${doc.getString("nombre")}")
        val opcion = funcionesExtra.leerDato(variables.menuEdicionCerveza, Int::class.java)
        if (opcion == null || opcion !in 0..6) {
            println("Opción no válida.")
            return
        }
        if (opcion == 0) {
            println("Actualización cancelada.")
            return
        }
        val update = when (opcion) {
            1 -> Updates.set("nombre", funcionesExtra.leerDato("Nuevo nombre: ", String::class.java))
            2 -> Updates.set("graduacion", funcionesExtra.leerDato("Nueva graduación: ", Double::class.java))
            3 -> Updates.set("tipo", funcionesExtra.leerDato("Nuevo tipo: ", String::class.java))
            4 -> Updates.set("color", funcionesExtra.leerDato("Nuevo color: ", String::class.java))
            5 -> Updates.set("origen", funcionesExtra.leerDato("Nuevo origen: ", String::class.java))
            6 -> Updates.set("puntuacion", funcionesExtra.leerDato("Nueva graduación: ", Double::class.java))
            else -> null
        }
        if (update != null) {
            val resultado = collection.updateOne(Document("idCerveza", idCerveza), update)
            if (resultado.modifiedCount > 0) {
                println("Cerveza con ID = $idCerveza actualizada con éxito.")
            } else {
                println("No se pudo actualizar la cerveza.")
            }
        }
    }
    fun eliminarCerveza(collection: MongoCollection<Document>) {
        imprimirCervezas(collection)
        val idCerveza = funcionesExtra.leerDato("Introduce ID de la cerveza a eliminar: ", Int::class.java)
        val doc = collection.find(Document("idCerveza", idCerveza)).first()
        if (doc == null) {
            println("No existe ninguna cerveza con ID = $idCerveza")
            return
        }
        val nombre = doc.getString("nombre")
        print("El nombre de la cerveza con ID $idCerveza es '$nombre'. ¿Quieres eliminarla? (s/n): ")
        val respuesta = readLine()?.lowercase()
        if (respuesta != "s") {
            println("Eliminación cancelada.")
            return
        }
        val resultado = collection.deleteOne(Document("idCerveza", idCerveza))
        if (resultado.deletedCount > 0) {
            println("Cerveza '$nombre' eliminada correctamente.")
        } else {
            println("No se pudo eliminar la cerveza.")
        }
    }
    fun consultarConFiltros(collection: MongoCollection<Document>) {
        println("\n=== Cervezas con graduación > 5 ===")
        val cervezasFuertes = collection.find(Filters.gt("graduacion", 5.0))
        for (doc in cervezasFuertes) {
            println("${doc.getString("nombre")} - Graduación: ${doc.get("graduacion", Number::class.java).toDouble()}")
        }
        println("\n=== Cervezas tipo Lager ===")
        val cervezasLager = collection.find(Filters.eq("tipo", "Lager"))
        for (doc in cervezasLager) {
            println(doc.getString("nombre"))
        }
    }
    fun consultarConProyeccion(collection: MongoCollection<Document>) {
        println("\n=== Cervezas con solo nombre y graduación ===")
        val proyeccion = collection.find()
            .projection(Projections.include("nombre", "graduacion"))
        for (doc in proyeccion) {
            println("Nombre: ${doc.getString("nombre")} - Graduación: ${doc.get("graduacion", Number::class.java).toDouble()}")
        }
    }
    fun calcularPromedioGraduacion(collection: MongoCollection<Document>) {
        val promedio = collection.aggregate(
            listOf(Aggregates.group(null, Accumulators.avg("promedio", "\$graduacion")))
        ).first()
        println("\nPromedio de graduación de todas las cervezas: ${promedio?.getDouble("promedio")}")
    }
    fun promedioPuntuacionPorTipo(collection: MongoCollection<Document>) {
        println("\n=== Promedio de puntuación por tipo de cerveza ===")
        val promedioPorTipo = collection.aggregate(
            listOf(Aggregates.group("\$tipo", Accumulators.avg("promedioPuntuacion", "\$puntuacion")))
        )
        for (doc in promedioPorTipo) {
            println("Tipo: ${doc.getString("_id")} - Promedio: ${doc.getDouble("promedioPuntuacion")}")
        }
    }
    fun top3Cervezas(collection: MongoCollection<Document>) {
        println("\n=== Top 3 cervezas por puntuación ===")
        val top3 = collection.aggregate(
            listOf(
                Aggregates.sort(Sorts.descending("puntuacion")),
                Aggregates.limit(3)
            )
        )
        for (doc in top3) {
            println("${doc.getString("nombre")} - Puntuación: ${doc.get("puntuacion", Number::class.java).toDouble()}")
        }
    }
}
val funcionesCervezas = FuncionesCerveza()

class FuncionesExtra {
    // Fin para cualquier when
    fun finEleccion(): Boolean {
        return true
    }
    /* Función para ahorrar líneas que se parece a Python al pedir datos
    * En la que puedes escribir un mensaje a mostrar*/
    fun input(mensaje: String): String? {
        print(mensaje)
        return readlnOrNull()
    }
    // Función genérica para pedir datos seguros
    /* Formato de escritura:
    * "Mensaje entre comillas que mostrará", Int.String.Double::class.java, valor por defecto*/
    @Suppress("Unchecked_cast") // Elimina los warnings de las T
    fun <T> leerDato(mensaje: String, tipo: Class<T>, valorPorDefecto: T? = null): T {
        while (true) {
            val inputUsuario = input(mensaje)
            try {
                return when (tipo) {
                    String::class.java -> (inputUsuario ?: valorPorDefecto ?: "") as T
                    Int::class.java -> (inputUsuario?.toIntOrNull() ?: throw Exception("No es un número entero")) as T
                    Double::class.java -> (inputUsuario?.toDoubleOrNull() ?: throw Exception("No es un número válido")) as T
                    else -> throw Exception("Tipo no soportado")
                }
            } catch (e: kotlin.Exception) {
                println("Error: ${e.message}. Por favor, introduce un valor válido.")
            }
        }
    }
}
val funcionesExtra = FuncionesExtra()
