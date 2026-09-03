package com.ontour;

import com.ontour.dao.ShowDAO;
import com.ontour.modelo.Show;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ShowDAO showDAO = new ShowDAO();
    private static final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

    public static void main(String[] args) {
        int opcion;

        do {
            mostrarMenu();
            opcion = leerEntero("Elegí una opción: ");

            switch (opcion) {
                case 1 -> registrarShow();
                case 2 -> consultarShows();
                case 3 -> modificarShow();
                case 4 -> eliminarShow();
                case 5 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opción inválida, intentá de nuevo.");
            }

        } while (opcion != 5);
    }

    private static void mostrarMenu() {
        System.out.println("\n===== ON TOUR - Gestión de Shows =====");
        System.out.println("1. Registrar show");
        System.out.println("2. Consultar shows");
        System.out.println("3. Modificar show");
        System.out.println("4. Eliminar show");
        System.out.println("5. Salir");
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
        showDAO.insertar(nuevoShow);
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
        showDAO.modificar(showModificado);
    }

    private static void eliminarShow() {
        System.out.println("\n--- Eliminar show ---");
        consultarShows();
        int id = leerEntero("\nIngresá el ID del show a eliminar: ");
        showDAO.eliminar(id);
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
            } catch (Exception e) {
                System.out.print("Formato inválido, usá dd/MM/yyyy: ");
            }
        }
    }

    private static LocalTime leerHora(String mensaje) {
        System.out.print(mensaje);
        while (true) {
            try {
                return LocalTime.parse(scanner.nextLine(), formatoHora);
            } catch (Exception e) {
                System.out.print("Formato inválido, usá HH:mm: ");
            }
        }
    }
}