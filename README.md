# On Tour

Sistema de gestión de riders y logística para giras musicales, desarrollado en el marco del Seminario de Práctica de Informática (Universidad Siglo 21).

## Contexto del proyecto

"On Tour" surge de una problemática real experimentada por el autor en su rol de tour manager de la banda Ráfaga, durante la gira "El Mundo Baila Ráfaga: Camino a los 30". El sistema busca centralizar la información operativa de cada show (riders técnicos y de hospitality, proveedores, viáticos, alojamiento y datos de integrantes), reemplazando el uso disperso de planillas, documentos compartidos y mensajería instantánea.

## Estado actual del prototipo

Esta es la primera versión funcional del sistema, que implementa un **CRUD completo** (Create, Read, Update, Delete) para la entidad **Show**, como punto de partida del sistema.

## Tecnologías utilizadas

- **IntelliJ IDEA** 
- **IntelliJ DataGrip**
- **Java** (JDK 26)
- **MySQL** como motor de base de datos
- **JDBC** (mysql-connector-j) para la conexión entre Java y MySQL
- **Maven** como gestor de dependencias

## Estructura del proyecto

src/main/java/com/ontour/
├── Main.java # Punto de entrada, menú interactivo por consola
├── modelo/
│ └── Show.java # Clase que representa la entidad Show
├── conexion/
│ └── ConexionBD.java # Clase que centraliza la conexión JDBC a MySQL
└── dao/
└── ShowDAO.java # Lógica de acceso a datos (insertar, consultar, modificar, eliminar)


## Base de datos

El script SQL para crear la base de datos y la tabla utilizada es el siguiente:

```sql
CREATE DATABASE IF NOT EXISTS ontour;
USE ontour;

CREATE TABLE shows (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_venue VARCHAR(150) NOT NULL,
    ciudad VARCHAR(100) NOT NULL,
    pais VARCHAR(100) NOT NULL,
    fecha DATE NOT NULL,
    hora_llegada TIME,
    hora_soundcheck TIME,
    hora_show TIME
);
```

## Cómo ejecutar el proyecto

1. Tener MySQL instalado y corriendo localmente.
2. Ejecutar el script SQL de arriba para crear la base de datos y la tabla.
3. Verificar los datos de conexión en `ConexionBD.java` (usuario, contraseña, URL) y ajustarlos según tu entorno local si es necesario.
4. Clonar este repositorio y abrirlo como proyecto Maven en un IDE (IntelliJ IDEA recomendado).
5. Ejecutar la clase `Main.java`.
6. Usar el menú interactivo por consola para registrar, consultar, modificar y eliminar shows.

## Funcionalidades del menú

1. Registrar show
2. Consultar shows
3. Modificar show
4. Eliminar show
5. Salir

## Autor

Valentín Ragusa - Legajo VINF016492
Licenciatura en Informática - Universidad Siglo 21