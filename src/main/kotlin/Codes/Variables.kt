package org.example.Codes

class Variables {
    // Ruta del archivo de BD
    val urlBD = "jdbc:mysql://ec2-98-90-151-148.compute-1.amazonaws.com:3306/LaBirradeBrian"
    val user = "AlvaroGM"
    val pass = "AlvaroGM"
    // Variables de inicio
    val titulo = "La Birra de Brian: El Renacer"
    val menuInicial = "\n" + """Menu principal:
    1. Leer información
    2. Insertar información
    3. Modificar información
    4. Eliminar información
    0. Salir""".trimIndent()
    var salirMenuInicial = false
    val finPrograma = "\nFin de programa"
}
val variables = Variables()