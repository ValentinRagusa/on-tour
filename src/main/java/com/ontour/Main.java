package com.ontour;

import com.ontour.conexion.ConexionBD;
import com.ontour.dao.*;
import com.ontour.excepcion.OnTourException;
import com.ontour.modelo.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

    // ShowDAO y ViaticoDAO quedan con su tipo concreto porque Main usa metodos que no estan
    // en la interfaz (consultarEliminados, restaurar, marcarComoPagado).
    private static final ShowDAO showDAO = new ShowDAO();
    private static final ViaticoDAO viaticoDAO = new ViaticoDAO();

    // Estos solo se usan para operaciones CRUD, asi que se declaran con el tipo de la interfaz.
    // A Main no le importa que clase concreta hay detras, solo que cumpla con CrudDAO.
    private static final CrudDAO<Rider> riderDAO = new RiderDAO();
    private static final CrudDAO<Contrarider> contrariderDAO = new ContrariderDAO();
    private static final CrudDAO<Proveedor> proveedorDAO = new ProveedorDAO();
    private static final CrudDAO<Integrante> integranteDAO = new IntegranteDAO();
    private static final CrudDAO<Habitacion> habitacionDAO = new HabitacionDAO();
    private static final CrudDAO<DocumentoIntegrante> documentoIntegranteDAO = new DocumentoIntegranteDAO();

    // DAOs de las tablas de union (no implementan CrudDAO)
    private static final ShowProveedorDAO showProveedorDAO = new ShowProveedorDAO();
    private static final HabitacionIntegranteDAO habitacionIntegranteDAO = new HabitacionIntegranteDAO();

    // ===== Arreglos para el Resumen general =====
    // Son 8 entidades fijas, por eso alcanza con un arreglo comun en vez de un ArrayList.
    // Los dos arreglos van en paralelo: NOMBRES_ENTIDADES[i] es el nombre del DAO que esta en DAOS[i].
    private static final CrudDAO<?>[] DAOS = {
            showDAO, riderDAO, contrariderDAO, proveedorDAO,
            integranteDAO, viaticoDAO, habitacionDAO, documentoIntegranteDAO
    };
    private static final String[] NOMBRES_ENTIDADES = {
            "Shows", "Riders", "Contrariders", "Proveedores",
            "Integrantes", "Viáticos", "Habitaciones", "Documentos de integrantes"
    };

    // ===== Opciones de los campos ENUM =====
    private static final String[] TIPOS_RIDER = {"Técnico", "Hospitality"};
    private static final String[] VALORES_TIPOS_RIDER = {"tecnico", "hospitality"};

    private static final String[] ESTADOS_RIDER = {"Pendiente", "En negociación", "Confirmado"};
    private static final String[] VALORES_ESTADOS_RIDER = {"pendiente", "en_negociacion", "confirmado"};

    private static final String[] DISPONIBILIDADES_CONTRARIDER = {"Confirmado", "Alternativa", "No disponible"};
    private static final String[] VALORES_DISPONIBILIDADES_CONTRARIDER = {"confirmado", "alternativa", "no_disponible"};

    private static final String[] TIPOS_PROVEEDOR = {"Transporte", "Alojamiento", "Técnico"};
    private static final String[] VALORES_TIPOS_PROVEEDOR = {"transporte", "alojamiento", "tecnico"};

    private static final String[] TIPOS_HABITACION = {"Individual", "Doble", "Triple"};
    private static final String[] VALORES_TIPOS_HABITACION = {"individual", "doble", "triple"};

    public static void main(String[] args) {
        // Antes de mostrar el menu se prueba la conexion, asi si falta db.properties
        // o MySQL no esta corriendo el usuario se entera de entrada y no a mitad de una carga
        try (Connection conexion = ConexionBD.obtenerConexion()) {
            System.out.println("Conexión con la base de datos establecida.");
        } catch (SQLException | OnTourException e) {
            System.out.println("No se pudo conectar con la base de datos: " + e.getMessage());
            return;
        }

        int opcion;

        do {
            mostrarMenuPrincipal();
            opcion = leerEntero("Elegí una opción: ");

            switch (opcion) {
                case 1 -> menuShows();
                case 2 -> menuRiders();
                case 3 -> menuContrariders();
                case 4 -> menuProveedores();
                case 5 -> menuIntegrantes();
                case 6 -> menuViaticos();
                case 7 -> menuHabitaciones();
                case 8 -> menuDocumentos();
                case 9 -> menuAsignarProveedorAShow();
                case 10 -> menuAsignarIntegranteAHabitacion();
                case 11 -> mostrarResumenGeneral();
                case 12 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opción inválida, intentá de nuevo.");
            }

        } while (opcion != 12);
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\n===== ON TOUR - Menú Principal =====");
        System.out.println("1. Shows");
        System.out.println("2. Riders");
        System.out.println("3. Contrariders");
        System.out.println("4. Proveedores");
        System.out.println("5. Integrantes");
        System.out.println("6. Viáticos");
        System.out.println("7. Habitaciones");
        System.out.println("8. Documentos de integrantes");
        System.out.println("9. Asignar proveedor a show");
        System.out.println("10. Asignar integrante a habitación");
        System.out.println("11. Resumen general");
        System.out.println("12. Salir");
    }

    // Recorre los dos arreglos con el mismo indice. Cada DAO se usa a traves de la interfaz,
    // y consultarTodos() ejecuta la implementacion del DAO que haya en esa posicion.
    private static void mostrarResumenGeneral() {
        System.out.println("\n===== Resumen general =====");
        for (int i = 0; i < DAOS.length; i++) {
            int cantidad = DAOS[i].consultarTodos().size();
            System.out.println(NOMBRES_ENTIDADES[i] + ": " + cantidad);
        }
    }

    // Metodo generico: sirve para listar cualquier entidad cuyo DAO implemente CrudDAO.
    // Reemplaza a los consultarRiders(), consultarProveedores(), etc. que eran todos iguales.
    private static <T> void listar(CrudDAO<T> dao, String titulo) {
        System.out.println("\n--- " + titulo + " ---");
        List<T> registros = dao.consultarTodos();

        if (registros.isEmpty()) {
            System.out.println("No hay registros cargados.");
        } else {
            for (T registro : registros) {
                System.out.println(registro);
            }
        }
    }

    // ============================================================
    // SHOWS
    // ============================================================

    private static void menuShows() {
        int opcion;
        do {
            System.out.println("\n===== Gestión de Shows =====");
            System.out.println("1. Registrar show");
            System.out.println("2. Consultar shows");
            System.out.println("3. Modificar show");
            System.out.println("4. Eliminar show");
            System.out.println("5. Ver shows eliminados");
            System.out.println("6. Restaurar show");
            System.out.println("7. Ver shows ordenados por fecha");
            System.out.println("8. Buscar show por venue o ciudad");
            System.out.println("9. Volver al menú principal");
            opcion = leerEntero("Elegí una opción: ");

            switch (opcion) {
                case 1 -> registrarShow();
                case 2 -> consultarShows();
                case 3 -> modificarShow();
                case 4 -> eliminarShow();
                case 5 -> consultarShowsEliminados();
                case 6 -> restaurarShow();
                case 7 -> consultarShowsOrdenados();
                case 8 -> buscarShows();
                case 9 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida, intentá de nuevo.");
            }
        } while (opcion != 9);
    }

    private static void registrarShow() {
        System.out.println("\n--- Registrar nuevo show ---");
        String venue = leerTexto("Nombre del venue: ");
        String ciudad = leerTexto("Ciudad: ");
        String pais = leerTexto("País: ");
        LocalDate fecha = leerFecha("Fecha del show (dd/MM/yyyy): ");
        LocalTime horaLlegada = leerHora("Hora de llegada (HH:mm): ");
        LocalTime horaSoundcheck = leerHora("Hora de soundcheck (HH:mm): ");
        LocalTime horaShow = leerHora("Hora del show (HH:mm): ");

        Show nuevoShow = new Show(venue, ciudad, pais, fecha, horaLlegada, horaSoundcheck, horaShow);

        // Si el soundcheck no es anterior al show, el DAO lanza OnTourException
        try {
            showDAO.insertar(nuevoShow);
        } catch (OnTourException e) {
            System.out.println("Atención: " + e.getMessage());
        }
    }

    private static void consultarShows() {
        System.out.println("\n--- Listado de shows ---");
        List<Show> shows = showDAO.consultarTodos();

        if (shows.isEmpty()) {
            System.out.println("No hay shows registrados.");
        } else {
            for (Show s : shows) {
                System.out.println(s);
            }
        }
    }

    private static void modificarShow() {
        System.out.println("\n--- Modificar show ---");
        consultarShows();
        int id = leerEntero("\nIngresá el ID del show a modificar: ");

        String venue = leerTexto("Nuevo nombre del venue: ");
        String ciudad = leerTexto("Nueva ciudad: ");
        String pais = leerTexto("Nuevo país: ");
        LocalDate fecha = leerFecha("Nueva fecha (dd/MM/yyyy): ");
        LocalTime horaLlegada = leerHora("Nueva hora de llegada (HH:mm): ");
        LocalTime horaSoundcheck = leerHora("Nueva hora de soundcheck (HH:mm): ");
        LocalTime horaShow = leerHora("Nueva hora del show (HH:mm): ");

        Show showModificado = new Show(id, venue, ciudad, pais, fecha, horaLlegada, horaSoundcheck, horaShow);
        try {
            showDAO.modificar(showModificado);
        } catch (OnTourException e) {
            System.out.println("Atención: " + e.getMessage());
        }
    }

    private static void eliminarShow() {
        System.out.println("\n--- Eliminar show ---");
        consultarShows();
        int id = leerEntero("\nIngresá el ID del show a eliminar: ");
        showDAO.eliminar(id);
    }

    private static void consultarShowsEliminados() {
        System.out.println("\n--- Listado de shows eliminados ---");
        List<Show> shows = showDAO.consultarEliminados();

        if (shows.isEmpty()) {
            System.out.println("No hay shows eliminados.");
        } else {
            for (Show s : shows) {
                System.out.println(s);
            }
        }
    }

    private static void restaurarShow() {
        System.out.println("\n--- Restaurar show ---");
        consultarShowsEliminados();
        int id = leerEntero("\nIngresá el ID del show a restaurar: ");
        showDAO.restaurar(id);
    }

    // Ordena la lista en memoria con Collections.sort(), que usa el compareTo() definido en Show
    private static void consultarShowsOrdenados() {
        System.out.println("\n--- Shows ordenados por fecha (del más próximo al más lejano) ---");
        List<Show> shows = showDAO.consultarTodos();

        if (shows.isEmpty()) {
            System.out.println("No hay shows registrados.");
            return;
        }

        Collections.sort(shows);
        for (Show s : shows) {
            System.out.println(s);
        }
    }

    // Busqueda lineal en memoria: recorre la lista y se queda con los shows
    // cuyo venue o ciudad contengan el texto ingresado (sin distinguir mayusculas/minusculas)
    private static void buscarShows() {
        System.out.println("\n--- Buscar show por venue o ciudad ---");
        String texto = leerTexto("Texto a buscar: ").trim().toLowerCase();

        if (texto.isEmpty()) {
            System.out.println("Tenés que ingresar al menos un carácter para buscar.");
            return;
        }

        List<Show> shows = showDAO.consultarTodos();
        List<Show> resultados = new ArrayList<>();

        for (Show s : shows) {
            boolean coincideVenue = s.getNombreVenue().toLowerCase().contains(texto);
            boolean coincideCiudad = s.getCiudad().toLowerCase().contains(texto);
            if (coincideVenue || coincideCiudad) {
                resultados.add(s);
            }
        }

        if (resultados.isEmpty()) {
            System.out.println("No se encontraron shows que coincidan con \"" + texto + "\".");
        } else {
            System.out.println("Se encontraron " + resultados.size() + " show(s):");
            for (Show s : resultados) {
                System.out.println(s);
            }
        }
    }

    // ============================================================
    // RIDERS
    // ============================================================

    private static void menuRiders() {
        int opcion;
        do {
            System.out.println("\n===== Gestión de Riders =====");
            System.out.println("1. Registrar rider");
            System.out.println("2. Consultar riders");
            System.out.println("3. Modificar rider");
            System.out.println("4. Eliminar rider");
            System.out.println("5. Volver al menú principal");
            opcion = leerEntero("Elegí una opción: ");

            switch (opcion) {
                case 1 -> registrarRider();
                case 2 -> listar(riderDAO, "Listado de riders");
                case 3 -> modificarRider();
                case 4 -> eliminarRider();
                case 5 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida, intentá de nuevo.");
            }
        } while (opcion != 5);
    }

    private static void registrarRider() {
        System.out.println("\n--- Registrar nuevo rider ---");

        System.out.println("Shows disponibles:");
        showDAO.mostrarResumen();
        int showId = leerEntero("ID del show: ");

        System.out.println("Tipo de rider:");
        String tipo = elegirOpcion(TIPOS_RIDER, VALORES_TIPOS_RIDER);

        LocalDate fechaCarga = leerFecha("Fecha de carga (dd/MM/yyyy): ");

        System.out.println("Estado del rider:");
        String estado = elegirOpcion(ESTADOS_RIDER, VALORES_ESTADOS_RIDER);

        Rider nuevoRider = RiderFactory.crear(0, showId, tipo, fechaCarga, estado);
        riderDAO.insertar(nuevoRider);
    }

    private static void modificarRider() {
        System.out.println("\n--- Modificar rider ---");
        listar(riderDAO, "Listado de riders");
        int id = leerEntero("\nIngresá el ID del rider a modificar: ");

        System.out.println("Shows disponibles:");
        showDAO.mostrarResumen();
        int showId = leerEntero("Nuevo ID del show: ");

        System.out.println("Nuevo tipo de rider:");
        String tipo = elegirOpcion(TIPOS_RIDER, VALORES_TIPOS_RIDER);

        LocalDate fechaCarga = leerFecha("Nueva fecha de carga (dd/MM/yyyy): ");

        System.out.println("Nuevo estado del rider:");
        String estado = elegirOpcion(ESTADOS_RIDER, VALORES_ESTADOS_RIDER);

        Rider riderModificado = RiderFactory.crear(id, showId, tipo, fechaCarga, estado);
        riderDAO.modificar(riderModificado);
    }

    private static void eliminarRider() {
        System.out.println("\n--- Eliminar rider ---");
        listar(riderDAO, "Listado de riders");
        int id = leerEntero("\nIngresá el ID del rider a eliminar: ");
        riderDAO.eliminar(id);
    }

    // ============================================================
    // CONTRARIDERS
    // ============================================================

    private static void menuContrariders() {
        int opcion;
        do {
            System.out.println("\n===== Gestión de Contrariders =====");
            System.out.println("1. Registrar contrarider");
            System.out.println("2. Consultar contrariders");
            System.out.println("3. Modificar contrarider");
            System.out.println("4. Eliminar contrarider");
            System.out.println("5. Volver al menú principal");
            opcion = leerEntero("Elegí una opción: ");

            switch (opcion) {
                case 1 -> registrarContrarider();
                case 2 -> listar(contrariderDAO, "Listado de contrariders");
                case 3 -> modificarContrarider();
                case 4 -> eliminarContrarider();
                case 5 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida, intentá de nuevo.");
            }
        } while (opcion != 5);
    }

    private static void registrarContrarider() {
        System.out.println("\n--- Registrar nuevo contrarider ---");

        System.out.println("Riders disponibles:");
        riderDAO.mostrarResumen();
        int riderId = leerEntero("ID del rider: ");

        int version = leerEntero("Número de versión: ");
        LocalDate fechaCarga = leerFecha("Fecha de carga (dd/MM/yyyy): ");
        String descripcion = leerTexto("Descripción: ");

        System.out.println("Disponibilidad:");
        String disponibilidad = elegirOpcion(DISPONIBILIDADES_CONTRARIDER, VALORES_DISPONIBILIDADES_CONTRARIDER);

        Contrarider nuevoContrarider = new Contrarider(riderId, version, fechaCarga, descripcion, disponibilidad);
        contrariderDAO.insertar(nuevoContrarider);
    }

    private static void modificarContrarider() {
        System.out.println("\n--- Modificar contrarider ---");
        listar(contrariderDAO, "Listado de contrariders");
        int id = leerEntero("\nIngresá el ID del contrarider a modificar: ");

        System.out.println("Riders disponibles:");
        riderDAO.mostrarResumen();
        int riderId = leerEntero("Nuevo ID del rider: ");

        int version = leerEntero("Nuevo número de versión: ");
        LocalDate fechaCarga = leerFecha("Nueva fecha de carga (dd/MM/yyyy): ");
        String descripcion = leerTexto("Nueva descripción: ");

        System.out.println("Nueva disponibilidad:");
        String disponibilidad = elegirOpcion(DISPONIBILIDADES_CONTRARIDER, VALORES_DISPONIBILIDADES_CONTRARIDER);

        Contrarider contrariderModificado = new Contrarider(id, riderId, version, fechaCarga, descripcion, disponibilidad);
        contrariderDAO.modificar(contrariderModificado);
    }

    private static void eliminarContrarider() {
        System.out.println("\n--- Eliminar contrarider ---");
        listar(contrariderDAO, "Listado de contrariders");
        int id = leerEntero("\nIngresá el ID del contrarider a eliminar: ");
        contrariderDAO.eliminar(id);
    }

    // ============================================================
    // PROVEEDORES
    // ============================================================

    private static void menuProveedores() {
        int opcion;
        do {
            System.out.println("\n===== Gestión de Proveedores =====");
            System.out.println("1. Registrar proveedor");
            System.out.println("2. Consultar proveedores");
            System.out.println("3. Modificar proveedor");
            System.out.println("4. Eliminar proveedor");
            System.out.println("5. Volver al menú principal");
            opcion = leerEntero("Elegí una opción: ");

            switch (opcion) {
                case 1 -> registrarProveedor();
                case 2 -> listar(proveedorDAO, "Listado de proveedores");
                case 3 -> modificarProveedor();
                case 4 -> eliminarProveedor();
                case 5 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida, intentá de nuevo.");
            }
        } while (opcion != 5);
    }

    private static void registrarProveedor() {
        System.out.println("\n--- Registrar nuevo proveedor ---");
        String nombre = leerTexto("Nombre: ");

        System.out.println("Tipo de proveedor:");
        String tipo = elegirOpcion(TIPOS_PROVEEDOR, VALORES_TIPOS_PROVEEDOR);

        String contacto = leerTexto("Contacto: ");

        Proveedor nuevoProveedor = new Proveedor(nombre, tipo, contacto);
        proveedorDAO.insertar(nuevoProveedor);
    }

    private static void modificarProveedor() {
        System.out.println("\n--- Modificar proveedor ---");
        listar(proveedorDAO, "Listado de proveedores");
        int id = leerEntero("\nIngresá el ID del proveedor a modificar: ");

        String nombre = leerTexto("Nuevo nombre: ");

        System.out.println("Nuevo tipo de proveedor:");
        String tipo = elegirOpcion(TIPOS_PROVEEDOR, VALORES_TIPOS_PROVEEDOR);

        String contacto = leerTexto("Nuevo contacto: ");

        Proveedor proveedorModificado = new Proveedor(id, nombre, tipo, contacto);
        proveedorDAO.modificar(proveedorModificado);
    }

    private static void eliminarProveedor() {
        System.out.println("\n--- Eliminar proveedor ---");
        listar(proveedorDAO, "Listado de proveedores");
        int id = leerEntero("\nIngresá el ID del proveedor a eliminar: ");
        proveedorDAO.eliminar(id);
    }

    // ============================================================
    // INTEGRANTES
    // ============================================================

    private static void menuIntegrantes() {
        int opcion;
        do {
            System.out.println("\n===== Gestión de Integrantes =====");
            System.out.println("1. Registrar integrante");
            System.out.println("2. Consultar integrantes");
            System.out.println("3. Modificar integrante");
            System.out.println("4. Eliminar integrante");
            System.out.println("5. Volver al menú principal");
            opcion = leerEntero("Elegí una opción: ");

            switch (opcion) {
                case 1 -> registrarIntegrante();
                case 2 -> listar(integranteDAO, "Listado de integrantes");
                case 3 -> modificarIntegrante();
                case 4 -> eliminarIntegrante();
                case 5 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida, intentá de nuevo.");
            }
        } while (opcion != 5);
    }

    private static void registrarIntegrante() {
        System.out.println("\n--- Registrar nuevo integrante ---");
        String nombre = leerTexto("Nombre: ");
        String documentoIdentidad = leerTexto("Documento de identidad: ");
        String numeroPasajeroFrecuente = leerTexto("Número de pasajero frecuente (opcional, Enter para omitir): ");

        if (numeroPasajeroFrecuente.isBlank()) {
            numeroPasajeroFrecuente = null;
        }

        Integrante nuevoIntegrante = new Integrante(nombre, documentoIdentidad, numeroPasajeroFrecuente);
        integranteDAO.insertar(nuevoIntegrante);
    }

    private static void modificarIntegrante() {
        System.out.println("\n--- Modificar integrante ---");
        listar(integranteDAO, "Listado de integrantes");
        int id = leerEntero("\nIngresá el ID del integrante a modificar: ");

        String nombre = leerTexto("Nuevo nombre: ");
        String documentoIdentidad = leerTexto("Nuevo documento de identidad: ");
        String numeroPasajeroFrecuente = leerTexto("Nuevo número de pasajero frecuente (opcional, Enter para omitir): ");

        if (numeroPasajeroFrecuente.isBlank()) {
            numeroPasajeroFrecuente = null;
        }

        Integrante integranteModificado = new Integrante(id, nombre, documentoIdentidad, numeroPasajeroFrecuente);
        integranteDAO.modificar(integranteModificado);
    }

    private static void eliminarIntegrante() {
        System.out.println("\n--- Eliminar integrante ---");
        listar(integranteDAO, "Listado de integrantes");
        int id = leerEntero("\nIngresá el ID del integrante a eliminar: ");
        integranteDAO.eliminar(id);
    }

    // ============================================================
    // VIÁTICOS
    // ============================================================

    private static void menuViaticos() {
        int opcion;
        do {
            System.out.println("\n===== Gestión de Viáticos =====");
            System.out.println("1. Registrar viático");
            System.out.println("2. Consultar viáticos");
            System.out.println("3. Modificar viático");
            System.out.println("4. Eliminar viático");
            System.out.println("5. Marcar viático como pagado");
            System.out.println("6. Volver al menú principal");
            opcion = leerEntero("Elegí una opción: ");

            switch (opcion) {
                case 1 -> registrarViatico();
                case 2 -> consultarViaticos();
                case 3 -> modificarViatico();
                case 4 -> eliminarViatico();
                case 5 -> marcarViaticoComoPagado();
                case 6 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida, intentá de nuevo.");
            }
        } while (opcion != 6);
    }

    private static void registrarViatico() {
        System.out.println("\n--- Registrar nuevo viático ---");

        System.out.println("Shows disponibles:");
        showDAO.mostrarResumen();
        int showId = leerEntero("ID del show: ");

        System.out.println("Integrantes disponibles:");
        integranteDAO.mostrarResumen();
        int integranteId = leerEntero("ID del integrante: ");

        BigDecimal monto = leerDecimal("Monto: ");
        LocalDate fecha = leerFecha("Fecha (dd/MM/yyyy): ");
        boolean pagado = leerBooleano("¿Ya fue pagado? (S/N): ");

        Viatico nuevoViatico = new Viatico(showId, integranteId, monto, fecha, pagado);
        viaticoDAO.insertar(nuevoViatico);
    }

    private static void consultarViaticos() {
        System.out.println("\n--- Listado de viáticos ---");
        List<Viatico> viaticos = viaticoDAO.consultarTodos();

        if (viaticos.isEmpty()) {
            System.out.println("No hay viáticos registrados.");
        } else {
            for (Viatico v : viaticos) {
                System.out.println(v);
            }
        }
    }

    private static void modificarViatico() {
        System.out.println("\n--- Modificar viático ---");
        consultarViaticos();
        int id = leerEntero("\nIngresá el ID del viático a modificar: ");

        System.out.println("Shows disponibles:");
        showDAO.mostrarResumen();
        int showId = leerEntero("Nuevo ID del show: ");

        System.out.println("Integrantes disponibles:");
        integranteDAO.mostrarResumen();
        int integranteId = leerEntero("Nuevo ID del integrante: ");

        BigDecimal monto = leerDecimal("Nuevo monto: ");
        LocalDate fecha = leerFecha("Nueva fecha (dd/MM/yyyy): ");
        boolean pagado = leerBooleano("¿Ya fue pagado? (S/N): ");

        Viatico viaticoModificado = new Viatico(id, showId, integranteId, monto, fecha, pagado);
        viaticoDAO.modificar(viaticoModificado);
    }

    private static void eliminarViatico() {
        System.out.println("\n--- Eliminar viático ---");
        consultarViaticos();
        int id = leerEntero("\nIngresá el ID del viático a eliminar: ");
        viaticoDAO.eliminar(id);
    }

    private static void marcarViaticoComoPagado() {
        System.out.println("\n--- Marcar viático como pagado ---");
        viaticoDAO.mostrarResumen();
        int id = leerEntero("\nIngresá el ID del viático a marcar como pagado: ");

        // Si el viatico ya estaba pagado, el DAO lanza OnTourException.
        // La capturamos aca para mostrar el mensaje sin que se caiga el programa.
        try {
            viaticoDAO.marcarComoPagado(id);
        } catch (OnTourException e) {
            System.out.println("Atención: " + e.getMessage());
        }
    }

    // ============================================================
    // HABITACIONES
    // ============================================================

    private static void menuHabitaciones() {
        int opcion;
        do {
            System.out.println("\n===== Gestión de Habitaciones =====");
            System.out.println("1. Registrar habitación");
            System.out.println("2. Consultar habitaciones");
            System.out.println("3. Modificar habitación");
            System.out.println("4. Eliminar habitación");
            System.out.println("5. Volver al menú principal");
            opcion = leerEntero("Elegí una opción: ");

            switch (opcion) {
                case 1 -> registrarHabitacion();
                case 2 -> listar(habitacionDAO, "Listado de habitaciones");
                case 3 -> modificarHabitacion();
                case 4 -> eliminarHabitacion();
                case 5 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida, intentá de nuevo.");
            }
        } while (opcion != 5);
    }

    private static void registrarHabitacion() {
        System.out.println("\n--- Registrar nueva habitación ---");

        System.out.println("Shows disponibles:");
        showDAO.mostrarResumen();
        int showId = leerEntero("ID del show: ");

        System.out.println("Tipo de habitación:");
        String tipo = elegirOpcion(TIPOS_HABITACION, VALORES_TIPOS_HABITACION);

        String numeroHabitacion = leerTexto("Número de habitación: ");

        Habitacion nuevaHabitacion = new Habitacion(showId, tipo, numeroHabitacion);
        habitacionDAO.insertar(nuevaHabitacion);
    }

    private static void modificarHabitacion() {
        System.out.println("\n--- Modificar habitación ---");
        listar(habitacionDAO, "Listado de habitaciones");
        int id = leerEntero("\nIngresá el ID de la habitación a modificar: ");

        System.out.println("Shows disponibles:");
        showDAO.mostrarResumen();
        int showId = leerEntero("Nuevo ID del show: ");

        System.out.println("Nuevo tipo de habitación:");
        String tipo = elegirOpcion(TIPOS_HABITACION, VALORES_TIPOS_HABITACION);

        String numeroHabitacion = leerTexto("Nuevo número de habitación: ");

        Habitacion habitacionModificada = new Habitacion(id, showId, tipo, numeroHabitacion);
        habitacionDAO.modificar(habitacionModificada);
    }

    private static void eliminarHabitacion() {
        System.out.println("\n--- Eliminar habitación ---");
        listar(habitacionDAO, "Listado de habitaciones");
        int id = leerEntero("\nIngresá el ID de la habitación a eliminar: ");
        habitacionDAO.eliminar(id);
    }

    // ============================================================
    // DOCUMENTOS DE INTEGRANTES
    // ============================================================

    private static void menuDocumentos() {
        int opcion;
        do {
            System.out.println("\n===== Gestión de Documentos de Integrantes =====");
            System.out.println("1. Registrar documento");
            System.out.println("2. Consultar documentos");
            System.out.println("3. Modificar documento");
            System.out.println("4. Eliminar documento");
            System.out.println("5. Volver al menú principal");
            opcion = leerEntero("Elegí una opción: ");

            switch (opcion) {
                case 1 -> registrarDocumento();
                case 2 -> listar(documentoIntegranteDAO, "Listado de documentos");
                case 3 -> modificarDocumento();
                case 4 -> eliminarDocumento();
                case 5 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida, intentá de nuevo.");
            }
        } while (opcion != 5);
    }

    private static void registrarDocumento() {
        System.out.println("\n--- Registrar nuevo documento ---");

        System.out.println("Integrantes disponibles:");
        integranteDAO.mostrarResumen();
        int integranteId = leerEntero("ID del integrante: ");

        String tipoDocumento = leerTexto("Tipo de documento (ej. DNI, Pasaporte, Visa): ");
        String archivo = leerTexto("Ruta o nombre del archivo (opcional, Enter para omitir): ");
        LocalDate fechaCarga = leerFecha("Fecha de carga (dd/MM/yyyy): ");

        if (archivo.isBlank()) {
            archivo = null;
        }

        DocumentoIntegrante nuevoDocumento = new DocumentoIntegrante(integranteId, tipoDocumento, archivo, fechaCarga);
        documentoIntegranteDAO.insertar(nuevoDocumento);
    }

    private static void modificarDocumento() {
        System.out.println("\n--- Modificar documento ---");
        listar(documentoIntegranteDAO, "Listado de documentos");
        int id = leerEntero("\nIngresá el ID del documento a modificar: ");

        System.out.println("Integrantes disponibles:");
        integranteDAO.mostrarResumen();
        int integranteId = leerEntero("Nuevo ID del integrante: ");

        String tipoDocumento = leerTexto("Nuevo tipo de documento (ej. DNI, Pasaporte, Visa): ");
        String archivo = leerTexto("Nueva ruta o nombre del archivo (opcional, Enter para omitir): ");
        LocalDate fechaCarga = leerFecha("Nueva fecha de carga (dd/MM/yyyy): ");

        if (archivo.isBlank()) {
            archivo = null;
        }

        DocumentoIntegrante documentoModificado = new DocumentoIntegrante(id, integranteId, tipoDocumento, archivo, fechaCarga);
        documentoIntegranteDAO.modificar(documentoModificado);
    }

    private static void eliminarDocumento() {
        System.out.println("\n--- Eliminar documento ---");
        listar(documentoIntegranteDAO, "Listado de documentos");
        int id = leerEntero("\nIngresá el ID del documento a eliminar: ");
        documentoIntegranteDAO.eliminar(id);
    }

    // ============================================================
    // ASIGNAR PROVEEDOR A SHOW (show_proveedor)
    // ============================================================

    private static void menuAsignarProveedorAShow() {
        int opcion;
        do {
            System.out.println("\n===== Asignar Proveedor a Show =====");
            System.out.println("1. Asignar proveedor a show");
            System.out.println("2. Consultar proveedores de un show");
            System.out.println("3. Eliminar asignación");
            System.out.println("4. Volver al menú principal");
            opcion = leerEntero("Elegí una opción: ");

            switch (opcion) {
                case 1 -> asignarProveedorAShow();
                case 2 -> consultarProveedoresDeShow();
                case 3 -> eliminarAsignacionProveedorShow();
                case 4 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida, intentá de nuevo.");
            }
        } while (opcion != 4);
    }

    private static void asignarProveedorAShow() {
        System.out.println("\n--- Asignar proveedor a show ---");

        System.out.println("Shows disponibles:");
        showDAO.mostrarResumen();
        int showId = leerEntero("ID del show: ");

        System.out.println("Proveedores disponibles:");
        proveedorDAO.mostrarResumen();
        int proveedorId = leerEntero("ID del proveedor: ");

        showProveedorDAO.asignar(showId, proveedorId);
    }

    private static void consultarProveedoresDeShow() {
        System.out.println("\n--- Proveedores asignados a un show ---");
        int showId = leerEntero("ID del show: ");
        List<Integer> proveedorIds = showProveedorDAO.consultarPorShow(showId);

        if (proveedorIds.isEmpty()) {
            System.out.println("Ese show no tiene proveedores asignados.");
        } else {
            System.out.println("IDs de proveedores asignados: " + proveedorIds);
        }
    }

    private static void eliminarAsignacionProveedorShow() {
        System.out.println("\n--- Eliminar asignación de proveedor a show ---");
        int showId = leerEntero("ID del show: ");
        int proveedorId = leerEntero("ID del proveedor: ");
        showProveedorDAO.eliminar(showId, proveedorId);
    }

    // ============================================================
    // ASIGNAR INTEGRANTE A HABITACIÓN (habitacion_integrante)
    // ============================================================

    private static void menuAsignarIntegranteAHabitacion() {
        int opcion;
        do {
            System.out.println("\n===== Asignar Integrante a Habitación =====");
            System.out.println("1. Asignar integrante a habitación");
            System.out.println("2. Consultar integrantes de una habitación");
            System.out.println("3. Eliminar asignación");
            System.out.println("4. Volver al menú principal");
            opcion = leerEntero("Elegí una opción: ");

            switch (opcion) {
                case 1 -> asignarIntegranteAHabitacion();
                case 2 -> consultarIntegrantesDeHabitacion();
                case 3 -> eliminarAsignacionIntegranteHabitacion();
                case 4 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida, intentá de nuevo.");
            }
        } while (opcion != 4);
    }

    private static void asignarIntegranteAHabitacion() {
        System.out.println("\n--- Asignar integrante a habitación ---");

        System.out.println("Habitaciones disponibles:");
        habitacionDAO.mostrarResumen();
        int habitacionId = leerEntero("ID de la habitación: ");

        System.out.println("Integrantes disponibles:");
        integranteDAO.mostrarResumen();
        int integranteId = leerEntero("ID del integrante: ");

        habitacionIntegranteDAO.asignar(habitacionId, integranteId);
    }

    private static void consultarIntegrantesDeHabitacion() {
        System.out.println("\n--- Integrantes asignados a una habitación ---");
        int habitacionId = leerEntero("ID de la habitación: ");
        List<Integer> integranteIds = habitacionIntegranteDAO.consultarPorHabitacion(habitacionId);

        if (integranteIds.isEmpty()) {
            System.out.println("Esa habitación no tiene integrantes asignados.");
        } else {
            System.out.println("IDs de integrantes asignados: " + integranteIds);
        }
    }

    private static void eliminarAsignacionIntegranteHabitacion() {
        System.out.println("\n--- Eliminar asignación de integrante a habitación ---");
        int habitacionId = leerEntero("ID de la habitación: ");
        int integranteId = leerEntero("ID del integrante: ");
        habitacionIntegranteDAO.eliminar(habitacionId, integranteId);
    }

    // ===== Métodos auxiliares de lectura =====

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    private static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        while (!scanner.hasNextInt()) {
            System.out.println("Por favor, ingresá un número válido.");
            scanner.next();
        }
        int valor = scanner.nextInt();
        scanner.nextLine();
        return valor;
    }

    private static LocalDate leerFecha(String mensaje) {
        System.out.print(mensaje);
        while (true) {
            try {
                return LocalDate.parse(scanner.nextLine(), formatoFecha);
            } catch (DateTimeParseException e) {
                System.out.print("Formato inválido, usá dd/MM/yyyy: ");
            }
        }
    }

    private static LocalTime leerHora(String mensaje) {
        System.out.print(mensaje);
        while (true) {
            try {
                return LocalTime.parse(scanner.nextLine(), formatoHora);
            } catch (DateTimeParseException e) {
                System.out.print("Formato inválido, usá HH:mm: ");
            }
        }
    }

    // Muestra las etiquetas numeradas de un campo ENUM y devuelve el valor real que se guarda en la base
    private static String elegirOpcion(String[] etiquetas, String[] valores) {
        for (int i = 0; i < etiquetas.length; i++) {
            System.out.println((i + 1) + ". " + etiquetas[i]);
        }

        int seleccion;
        do {
            seleccion = leerEntero("Elegí una opción: ");
            if (seleccion < 1 || seleccion > etiquetas.length) {
                System.out.println("Opción inválida, intentá de nuevo.");
            }
        } while (seleccion < 1 || seleccion > etiquetas.length);

        return valores[seleccion - 1];
    }

    private static BigDecimal leerDecimal(String mensaje) {
        System.out.print(mensaje);
        while (!scanner.hasNextBigDecimal()) {
            System.out.println("Por favor, ingresá un monto válido.");
            scanner.next();
        }
        BigDecimal valor = scanner.nextBigDecimal();
        scanner.nextLine();
        return valor;
    }

    private static boolean leerBooleano(String mensaje) {
        while (true) {
            String respuesta = leerTexto(mensaje).trim().toLowerCase();
            if (respuesta.equals("s") || respuesta.equals("si")) {
                return true;
            } else if (respuesta.equals("n") || respuesta.equals("no")) {
                return false;
            }
            System.out.println("Respuesta inválida, ingresá S o N.");
        }
    }
}
