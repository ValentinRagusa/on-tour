# On Tour

Sistema de gestión de riders y logística para giras musicales, desarrollado en el marco del Seminario de Práctica de Informática (Universidad Siglo 21).

## Contexto del proyecto

"On Tour" surge de una problemática real experimentada por el autor en su rol de tour manager de la banda Ráfaga, durante la gira "El Mundo Baila Ráfaga: Camino a los 30". El sistema busca centralizar la información operativa de cada show (riders técnicos y de hospitality, proveedores, viáticos, alojamiento y datos de integrantes), reemplazando el uso disperso de planillas, documentos compartidos y mensajería instantánea.

## Estado actual del prototipo

El sistema ya cubre las 10 entidades del modelo: **Show, Rider, Contrarider, Proveedor, Integrante, Viatico, Habitacion, DocumentoIntegrante**, más las relaciones **ShowProveedor** y **HabitacionIntegrante**. Cada entidad tiene operaciones de inserción, consulta, modificación y eliminación (Show, además, usa **borrado lógico/soft delete** en vez de borrado físico, con opción de ver y restaurar los shows eliminados).

Se suman las siguientes mejoras de usabilidad:

- **Listados de referencia**: antes de pedir un ID que referencia a otra entidad (por ejemplo el show al cargar un Rider, o el integrante al cargar un Viatico), el sistema muestra primero un resumen con las opciones disponibles.
- **Validación de duplicados**: restricciones UNIQUE en la base de datos evitan registros lógicamente duplicados (por ejemplo, dos riders del mismo tipo para el mismo show).
- **Mensajes de error claros**: ante un ID inexistente (violación de clave foránea) o un dato duplicado (violación de restricción UNIQUE), el sistema distingue ambos casos y muestra un mensaje amigable en vez del error crudo de SQL.

### Programación Orientada a Objetos

El sistema demuestra los 4 pilares de la POO:

- **Encapsulamiento**: todos los modelos tienen sus atributos privados, con acceso a través de getters y setters. El `id` no tiene setter porque lo asigna la base de datos.
- **Herencia**: `Rider` es una clase abstracta, con las subclases `RiderTecnico` y `RiderHospitality`, que heredan los atributos comunes (id, show, fecha de carga y estado) y reutilizan el constructor del padre mediante `super(...)`.
- **Polimorfismo**: el método `describirResponsable()` se sobrescribe en cada subclase (para el rider técnico, el responsable es el Jefe de Stage, Sonido e Iluminación; para el de hospitality, el Tour Manager y el Manager). `RiderDAO.consultarTodos()` devuelve una `List<Rider>` con instancias mezcladas de ambas subclases, y al recorrerla con una referencia de tipo `Rider` se ejecuta la versión del método que corresponde al tipo real de cada objeto (polimorfismo dinámico).
- **Abstracción**: la clase abstracta `Rider` declara los métodos abstractos `getTipo()` y `describirResponsable()`, que cada subclase implementa. Además, la interfaz genérica `CrudDAO<T>` define las operaciones comunes (`insertar`, `consultarTodos`, `modificar`, `eliminar`, `mostrarResumen`) y la implementan los 8 DAOs de entidades.

También se incorporaron:

- **Ordenamiento**: `Show` implementa `Comparable<Show>`, con un orden natural por fecha (del show más próximo al más lejano), que se usa con `Collections.sort()` para listar los shows ordenados.
- **Búsqueda**: búsqueda lineal en memoria de shows por venue o ciudad, recorriendo la lista ya cargada sin distinguir mayúsculas de minúsculas.
- **Excepciones propias**: `OnTourException` (que extiende `RuntimeException`) se usa para validar reglas de negocio, por ejemplo para evitar marcar dos veces un viático como pagado. La excepción se captura en el menú y se muestra un mensaje claro, sin que el programa se interrumpa.
- **Manejo específico de excepciones**: la lectura de fechas y horas captura `DateTimeParseException` en lugar de una excepción genérica.

## Tecnologías utilizadas

- **IntelliJ IDEA** 
- **IntelliJ DataGrip**
- **Java** (JDK 26)
- **MySQL** como motor de base de datos
- **JDBC** (mysql-connector-j) para la conexión entre Java y MySQL
- **Maven** como gestor de dependencias

## Estructura del proyecto

```
src/main/java/com/ontour/
├── Main.java                        # Punto de entrada, menú interactivo por consola
├── modelo/
│   ├── Show.java                    # Implementa Comparable<Show> (orden por fecha)
│   ├── Rider.java                   # Clase abstracta
│   ├── RiderTecnico.java            # Subclase de Rider
│   ├── RiderHospitality.java        # Subclase de Rider
│   ├── Contrarider.java
│   ├── Proveedor.java
│   ├── Integrante.java
│   ├── Viatico.java
│   ├── Habitacion.java
│   └── DocumentoIntegrante.java
├── conexion/
│   └── ConexionBD.java              # Clase que centraliza la conexión JDBC a MySQL
├── excepcion/
│   └── OnTourException.java         # Excepción propia para reglas de negocio
└── dao/
    ├── CrudDAO.java                 # Interfaz genérica CRUD que implementan los DAOs de entidades
    ├── ShowDAO.java
    ├── RiderDAO.java
    ├── ContrariderDAO.java
    ├── ProveedorDAO.java
    ├── IntegranteDAO.java
    ├── ViaticoDAO.java
    ├── HabitacionDAO.java
    ├── DocumentoIntegranteDAO.java
    ├── ShowProveedorDAO.java        # DAO de la relación show-proveedor (tabla de unión)
    └── HabitacionIntegranteDAO.java # DAO de la relación habitación-integrante (tabla de unión)
```

## Base de datos

El esquema completo (las 10 tablas del modelo con sus relaciones, restricciones UNIQUE y claves foráneas), junto con los datos de prueba y las consultas SQL de ejemplo, se encuentra en `database/on-tour-database.sql`.

## Cómo ejecutar el proyecto

1. Tener MySQL instalado y corriendo localmente.
2. Ejecutar `database/on-tour-database.sql` para crear la base de datos y las tablas.
3. Verificar los datos de conexión en `ConexionBD.java` (usuario, contraseña, URL) y ajustarlos según tu entorno local si es necesario.
4. Clonar este repositorio y abrirlo como proyecto Maven en un IDE (IntelliJ IDEA recomendado).
5. Ejecutar la clase `Main.java`.
6. Usar el menú interactivo por consola para gestionar shows, riders, contrariders, proveedores, integrantes, viáticos, habitaciones, documentos y sus relaciones.

## Funcionalidades del menú

El menú principal permite elegir qué entidad o relación gestionar. Cada opción abre un submenú propio:

- **Shows**: Registrar, Consultar, Modificar, Eliminar (borrado lógico), Ver shows eliminados, Restaurar show, Ver shows ordenados por fecha, Buscar show por venue o ciudad
- **Riders**: Registrar, Consultar (muestra además el responsable de cada rider según su tipo), Modificar, Eliminar
- **Contrariders**: Registrar, Consultar, Modificar, Eliminar
- **Proveedores**: Registrar, Consultar, Modificar, Eliminar
- **Integrantes**: Registrar, Consultar, Modificar, Eliminar
- **Viáticos**: Registrar, Consultar, Modificar, Eliminar, Marcar como pagado (valida que el viático no esté pagado previamente y, si ya lo estaba, lanza una excepción de negocio)
- **Habitaciones**: Registrar, Consultar, Modificar, Eliminar
- **Documentos de integrantes**: Registrar, Consultar, Modificar, Eliminar
- **Asignar proveedor a show**: Asignar, Consultar proveedores de un show, Eliminar asignación
- **Asignar integrante a habitación**: Asignar, Consultar integrantes de una habitación, Eliminar asignación

Cada submenú tiene además la opción de volver al menú principal, y desde ahí se puede salir del sistema.

## Autor

Valentín Ragusa - Legajo VINF016492
Licenciatura en Informática - Universidad Siglo 21
