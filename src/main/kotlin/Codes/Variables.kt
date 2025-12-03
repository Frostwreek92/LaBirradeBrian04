package org.example.Codes

class Variables {
    // Ruta del archivo de BD
    val urlBD = "jdbc:mysql://ec2-98-90-151-148.compute-1.amazonaws.com:3306/LaBirradeBrian"
    val user = "AlvaroGM"
    val pass = "AlvaroGM"
    val noConexion = "\nNo se ha podido establecer la conexión."

    // Variables de inicio
    val titulo = "La Birra de Brian: El Renacer"
    val menuInicial = "\n" + """Menu principal:
    1. Leer información
    2. Insertar información
    3. Modificar información
    4. Eliminar información
    0. Salir
    Introduce una opción entre 0 y 4: """.trimIndent()
    var salirMenuInicial = false
    val finPrograma = "\nFin de programa"
    val cervezasImpresion = "\n=== Cervezas ==="
    val menuEdicionCerveza = "\n" + """¿Qué deseas modificar?
    1. Nombre
    2. Graduación
    3. Tipo
    4. Color
    5. Origen
    6. Puntuación
    7. Cancelar
    Tu opción: """.trimIndent()

    // Query de las funciones
    // Cervezas
    val queryListarCervezas = "SELECT * FROM ListaCervezas"
    val queryInsertarCerveza = "INSERT INTO ListaCervezas(nombreCerveza, " +
            "graduacionCerveza, " +
            "tipoCerveza, " +
            "colorCerveza, " +
            "origenCerveza, " +
            "puntuacionCerveza) " +
            "VALUES (?, ?, ?, ?, ?, ?)"
    val querySeleccionarDatosIDCerveza = "SELECT nombreCerveza, " +
            "graduacionCerveza, " +
            "tipoCerveza, " +
            "colorCerveza, " +
            "origenCerveza, " +
            "puntuacionCerveza " +
            "FROM ListaCervezas " +
            "WHERE idCerveza = ?"
    val queryActualizarCerveza =
        "UPDATE ListaCervezas SET " +
                "nombreCerveza = ?, " +
                "graduacionCerveza = ?, " +
                "tipoCerveza = ?, " +
                "colorCerveza = ?, " +
                "origenCerveza = ?, " +
                "puntuacionCerveza = ? " +
                "WHERE idCerveza = ?"
    val queryEliminarCerveza = "DELETE FROM Cervezas WHERE idCerveza = ?"
}
val variables = Variables()