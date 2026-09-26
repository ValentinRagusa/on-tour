package com.ontour.dao;

import com.ontour.conexion.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Data Acces Object class para la tabla de union habitacion_integrante
public class HabitacionIntegranteDAO {
    // Asignar un integrante a una habitacion
    public void asignar(int habitacionId, int integranteId) {
        String sql = "INSERT INTO habitacion_integrante (habitacion_id, integrante_id) VALUES (?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, habitacionId);
            stmt.setInt(2, integranteId);

            stmt.executeUpdate();
            System.out.println("Integrante asignado a la habitación con éxito.");

        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("Ya existe un registro con estos mismos datos. Verificá que no sea un duplicado.");
            } else {
                System.out.println("El ID ingresado no corresponde a un registro existente. Revisá la lista de opciones mostrada arriba.");
            }
        } catch (SQLException e) {
            System.out.println("Error al asignar el integrante a la habitación: " + e.getMessage());
        }
    }

    // Consultar los IDs de integrantes asignados a una habitacion
    public List<Integer> consultarPorHabitacion(int habitacionId) {
        List<Integer> integranteIds = new ArrayList<>();
        String sql = "SELECT integrante_id FROM habitacion_integrante WHERE habitacion_id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, habitacionId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    integranteIds.add(rs.getInt("integrante_id"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar los integrantes de la habitación: " + e.getMessage());
        }

        return integranteIds;
    }

    // Eliminar la asignacion de un integrante a una habitacion
    public void eliminar(int habitacionId, int integranteId) {
        String sql = "DELETE FROM habitacion_integrante WHERE habitacion_id = ? AND integrante_id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, habitacionId);
            stmt.setInt(2, integranteId);

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Asignación eliminada con éxito.");
            } else {
                System.out.println("No se encontró esa asignación de integrante y habitación.");
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar la asignación: " + e.getMessage());
        }
    }
}
