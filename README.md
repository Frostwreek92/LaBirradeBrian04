# Proyecto: La Birra de Brian
Sistema CRUD en Kotlin con MongoDB  
Autor: *Álvaro García Mon*

---

## Descripción
Este proyecto es una aplicación de consola en Kotlin que gestiona una base de datos de cervezas usando MongoDB.  
Permite realizar operaciones CRUD completas, importar/exportar la base de datos a JSON y ejecutar consultas avanzadas mediante filtros, proyecciones y agregaciones.
---

# Estructura del proyecto

    src/main
    └── kotlin
        └── Codes/
        ├   ├── FuncionesCervezas.kt
        ├   ├── FuncionesExtra.kt
        ├   └── Variables.kt
        ├── Iniciar.kt
        └── Main.kt
    └── resources
        ├── cervezas.json
        └── simplelogger.properties


---

# Tecnologías utilizadas
- Kotlin
- MongoDB (Driver oficial)
- IntelliJ IDEA

---

## Cómo ejecutar
1. Abrir el proyecto en IntelliJ IDEA.
2. Iniciar el servidor de MongoDB (localhost:27017).
3. Ejecutar:

src/main/kotlin/org/example/Main.kt

---

## Menú principal

1. CRUD
2. Consultas Adicionales
3. Exportar Base de Datos
4. Importar Base de Datos
   0. Salir

---

#  1. CRUD
### Mostrar todas las cervezas
Lee todos los documentos de la colección y muestra cada campo de forma clara.

### Insertar una nueva cerveza
El usuario introduce todos los datos requeridos, generándose un nuevo `ObjectId`.

### Actualizar una cerveza
Se solicita el ID → busca la cerveza → y si existe pide los nuevos valores.

### Eliminar una cerveza
Se solicita el ID → se muestra la cerveza a borrar → pide confirmación.

---

# 2. Consultas adicionales
Esta sección demuestra todas las consultas pedidas en los criterios de evaluación.

### 1 Consultas con filtros (Filters.eq, Filters.gt, etc.)
Ejemplos implementados:

- Cervezas con graduación mayor que un valor (Filters.gt)
- Cervezas filtradas por tipo exacto (Filters.eq)

Cada consulta recorre los resultados y los muestra formateados.

---

### 2 Consultas con proyecciones (Projections.include)
Consultas que muestran solo campos específicos, por ejemplo:

- Mostrar únicamente: nombre – graduación
- Proyección de varios campos mediante:
  Projections.include("nombre", "graduacion", "puntuacion")

---

### 3 Cálculos usando aggregate()
Consultas que incluyen operaciones dentro de un pipeline:

- Promedio global de graduación

---

### 4 Cálculos usando aggregate() + $group
Ejemplos implementados:

- Promedio de puntuación agrupado por tipo

Ejemplo de salida:
Tipo: IPA → Promedio puntuación: 7.8
Tipo: Lager → Promedio puntuación: 6.5

---

### 5 Consultas con aggregate() + $limit
Implementado:

- Top 3 cervezas mejor puntuadas

---

# 3. Exportar Base de Datos
Permite guardar la colección completa en un archivo JSON:

resources/LaBirradeBrian.json

---

# 4. Importar Base de Datos
Carga un archivo JSON previamente exportado e inserta su contenido en la colección.  
Incluye validación de errores mediante try/catch.

resources/LaBirradeBrian.json