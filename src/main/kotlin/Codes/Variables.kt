package org.example.Codes

class Variables {
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