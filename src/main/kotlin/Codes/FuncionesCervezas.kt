package org.example.Codes

import java.sql.SQLException
import kotlin.use

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
    // Listar Cervezas
    fun listarCervezas(): MutableList<Cerveza>? {
        val lista = mutableListOf<Cerveza>()
        return try {
            funcionesExtra.getConnection()?.use { conn ->
                conn.createStatement().use { stmt ->
                    stmt.executeQuery(variables.queryListarCervezas).use { rs ->
                        while (rs.next()) {
                            lista.add(
                                Cerveza(
                                    idCerveza = rs.getInt("idCerveza"),
                                    nombreCerveza = rs.getString("nombreCerveza"),
                                    graduacionCerveza = rs.getDouble("graduacionCerveza"),
                                    tipoCerveza = rs.getString("tipoCerveza"),
                                    colorCerveza = rs.getString("colorCerveza"),
                                    origenCerveza = rs.getString("origenCerveza"),
                                    puntuacionCerveza = rs.getDouble("puntuacionCerveza")
                                )
                            )
                        }
                    }
                }
            } ?: run {
                println(variables.noConexion)
                return null
            }
            lista
        } catch (e: SQLException) {
            println("Error accediendo a la BD: ${e.message}")
            null
        }
    }
    // Insertar Cerveza
    fun insertarCerveza(cerveza: Cerveza) {
        try {
            funcionesExtra.getConnection()?.use { conn ->
                conn.prepareStatement(variables.queryInsertarCerveza).use { stmt ->
                    stmt.setString(1, cerveza.nombreCerveza)
                    stmt.setDouble(2, cerveza.graduacionCerveza)
                    stmt.setString(3, cerveza.tipoCerveza)
                    stmt.setString(4, cerveza.colorCerveza)
                    stmt.setString(5, cerveza.origenCerveza)
                    stmt.setDouble(6, cerveza.puntuacionCerveza)
                    stmt.executeUpdate()
                    println("\nCerveza '${cerveza.nombreCerveza}' insertada con éxito")
                }
            } ?: println(variables.noConexion)
        } catch (e: SQLException) {
            println("Error insertando Cerveza: ${e.message}")
            return
        }
    }
    // Actualizar Cerveza
    fun actualizarCerveza(idCerveza: Int) {
        if (idCerveza == null) {
            println("\nNo se puede actualizar la cerveza sin un ID.")
            return
        }
        try {
            funcionesExtra.getConnection()?.use { conn ->
                val selectStmt = conn.prepareStatement(variables.querySeleccionarDatosIDCerveza)
                selectStmt.setInt(1, idCerveza)
                val rs = selectStmt.executeQuery()
                if (!rs.next()) {
                    println("No existe ninguna cerveza con ese ID.")
                    return
                }
                var nombreCerveza = rs.getString("nombreCerveza")
                var graduacionCerveza = rs.getDouble("graduacionCerveza")
                var tipoCerveza = rs.getString("tipoCerveza")
                var colorCerveza = rs.getString("colorCerveza")
                var origenCerveza = rs.getString("origenCerveza")
                var puntuacionCerveza = rs.getDouble("puntuacionCerveza")
                val opcion = funcionesExtra.leerDato(variables.menuEdicionCerveza, Int::class.java)
                when (opcion) {
                    1 -> nombreCerveza = funcionesExtra.leerDato("Nuevo nombre: ", String::class.java)
                    2 -> graduacionCerveza = funcionesExtra.leerDato("Nueva graduación: ", Double::class.java)
                    3 -> tipoCerveza = funcionesExtra.leerDato("Nuevo tipo: ", String::class.java)
                    4 -> colorCerveza = funcionesExtra.leerDato("Nuevo color: ", String::class.java)
                    5 -> origenCerveza = funcionesExtra.leerDato("Nuevo origen: ", String::class.java)
                    6 -> puntuacionCerveza = funcionesExtra.leerDato("Nueva puntuación: ", Double::class.java)
                    7 -> {
                        println("Actualización cancelada.")
                        return
                    }
                    else -> {
                        println("Opción no válida.")
                        return
                    }
                }
                conn.prepareStatement(variables.queryActualizarCerveza).use { stmt ->
                    stmt.setString(1, nombreCerveza)
                    stmt.setDouble(2, graduacionCerveza)
                    stmt.setString(3, tipoCerveza)
                    stmt.setString(4, colorCerveza)
                    stmt.setString(5, origenCerveza)
                    stmt.setDouble(6, puntuacionCerveza)
                    stmt.setInt(7, idCerveza)
                    val filas = stmt.executeUpdate()
                    if (filas > 0) {
                        println("\nCerveza con ID = ${idCerveza} actualizada con éxito.")
                    } else {
                        println("\nNo se encontró ninguna cerveza con el ID = ${idCerveza}")
                    }
                }
            } ?: println(variables.noConexion)
        } catch (e: SQLException) {
            println("Error actualizando cerveza: ${e.message}")
            return
        }
    }
    // Eliminar Cerveza
    fun eliminarCerveza(idCerveza: Int) {
        try {
            funcionesExtra.getConnection()?.use { conn ->
                var nombre: String? = null
                conn.prepareStatement(variables.queryEliminarCerveza).use { pstmt ->
                    pstmt.setInt(1, idCerveza)
                    val rs = pstmt.executeQuery()

                    if (rs.next()) {
                        nombre = rs.getString("nombre")
                    }
                }
                if (nombre == null) {
                    println("No existe ninguna cerveza con ese ID")
                    return
                }
                val respuesta = funcionesExtra.input(
                    "El nombre de la cerveza con ID: $idCerveza es: $nombre ¿Quieres eliminarla? (s/n): "
                )?.lowercase()
                if (respuesta != "s") {
                    println("Eliminación cancelada")
                    return
                }
                conn.prepareStatement(variables.queryEliminarCerveza).use { pstmt ->
                    pstmt.setInt(1, idCerveza)
                    pstmt.executeUpdate()
                }
                println("Cerveza eliminada correctamente")
            } ?: println(variables.noConexion)
        } catch (e: SQLException) {
            println("Error eliminando cerveza: ${e.message}")
            return
        }
    }
}
class CervezasFunciones {
    fun imprimirCervezas() {
        val lista = CervezasDAO.listarCervezas()
        if (lista == null) {
            println("Hubo un error. Volviendo al menú anterior...")
            return
        }
        if (lista.isEmpty()) {
            println("No hay cervezas registradas.")
            return
        }
        println(variables.cervezasImpresion)
        lista.forEach {
            println("ID: ${it.idCerveza} " +
                    "- Nombre: ${it.nombreCerveza} " +
                    "- Graduacion: ${it.graduacionCerveza}% " +
                    "- Tipo: ${it.tipoCerveza} " +
                    "- Color: ${it.colorCerveza} " +
                    "- Origen: ${it.origenCerveza} " +
                    "- Puntuacion: ${it.puntuacionCerveza}*"
            )
        }
    }
    fun insertarCervezaBD(){
        CervezasDAO.insertarCerveza(
            Cerveza(
                nombreCerveza = funcionesExtra.leerDato("Introduce nombre: ", String::class.java, "Default"),
                graduacionCerveza = funcionesExtra.leerDato("Introduce graduación: ", Double::class.java, 0.0),
                tipoCerveza = funcionesExtra.leerDato("Introduce tipo: ", String::class.java, "Default"),
                colorCerveza = funcionesExtra.leerDato("Introduce color: ", String::class.java, "Default"),
                origenCerveza = funcionesExtra.leerDato("Introduce origen: ", String::class.java, "Default"),
                puntuacionCerveza = funcionesExtra.leerDato("Introduce puntuación: ", Double::class.java, 0.0)
            )
        )
    }
    fun actualizarCervezaPorID() {
        imprimirCervezas()
        val idCerveza = funcionesExtra.leerDato("Introduce ID de la Cerveza que quieres modificar: ", Int::class.java)
        CervezasDAO.actualizarCerveza(idCerveza)
    }
    fun eliminarCervezaPorID() {
        imprimirCervezas()
        val idCerveza = funcionesExtra.leerDato("Introduce ID de la Cerveza que quieres eliminar: ", Int::class.java)
        CervezasDAO.eliminarCerveza(idCerveza)
    }
}
val funcionesCervezas = CervezasFunciones()