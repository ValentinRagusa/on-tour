package com.ontour.dao;

import com.ontour.conexion.ConexionBD;
import com.ontour.modelo.Habitacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Data Acces Object class
public class HabitacionDAO {
    // Insertar nueva habitacion
    public void insertar(Habitacion habitacion) {
        String sql = "INSERT INTO habitaciones (show_id, tipo, numero_habitacion) VALUES (?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, habitacion.getShowId());
            stmt.setString(2, habitacion.getTipo());
            stmt.setString(3, habitacion.getNumeroHabitacion());

            stmt.executeUpdate();
            System.out.println("Habitación registrada con éxito.");

        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("Ya existe un registro con estos mismos datos. Verificá que no sea un duplicado.");
            } else {
                System.out.println("El ID ingresado no corresponde a un registro existente. Revisá la lista de opciones mostrada arriba.");
            }
        } catch (SQLException e) {
            System.out.println("Error al registrar la habitación: " + e.getMessage());
        }
    }

    // Consultar todas las habitaciones
    public List<Habitacion> consultarTodos() {
        List<Habitacion> habitaciones = new ArrayList<>();
        String sql = "SELECT * FROM habitaciones ORDER BY id";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                habitaciones.add(mapearHabitacion(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar las habitaciones: " + e.getMessage());
        }

        return habitaciones;
    }

    // Modificar una habitacion existente
    public void modificar(Habitacion habitacion) {
        String sql = "UPDATE habitaciones SET show_id = ?, tipo = ?, numero_habitacion = ? WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, habitacion.getShowId());
            stmt.setString(2, habitacion.getTipo());
            stmt.setString(3, habitacion.getNumeroHabitacion());
            stmt.setInt(4, habitacion.getId());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Habitación modificada con éxito.");
            } else {
                System.out.println("No se encontró una habitación con ese ID.");
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("Ya existe un registro con estos mismos datos. Verificá que no sea un duplicado.");
            } else {
                System.out.println("El ID ingresado no corresponde a un registro existente. Revisá la lista de opciones mostrada arriba.");
            }
        } catch (SQLException e) {
            System.out.println("Error al modificar la habitación: " + e.getMessage());
        }
    }

    // Eliminar una habitacion por ID
    public void eliminar(int id) {
        String sql = "DELETE FROM habitaciones WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Habitación eliminada con éxito.");
            } else {
                System.out.println("No se encontró una habitación con ese ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar la habitación: " + e.getMessage());
        }
    }

    // Muestra un resumen liviano (id + tipo) para elegir una habitación al completar otro formulario
    public void mostrarResumen() {
        String sql = "SELECT id, tipo, numero_habitacion FROM habitaciones ORDER BY id";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + " | Numero: " + rs.getString("numero_habitacion") +
                        " | Tipo: " + rs.getString("tipo"));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar el resumen de habitaciones: " + e.getMessage());
        }
    }

    // Metodo auxiliar para convertir una fila del ResultSet en un objeto Habitacion
    private Habitacion mapearHabitacion(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int showId = rs.getInt("show_id");
        String tipo = rs.getString("tipo");
        String numeroHabitacion = rs.getString("numero_habitacion");

        return new Habitacion(id, showId, tipo, numeroHabitacion);
    }
}
