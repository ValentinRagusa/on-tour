package com.ontour.dao;

import com.ontour.conexion.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Data Acces Object class para la tabla de union show_proveedor
public class ShowProveedorDAO {
    // Asignar un proveedor a un show
    public void asignar(int showId, int proveedorId) {
        String sql = "INSERT INTO show_proveedor (show_id, proveedor_id) VALUES (?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, showId);
            stmt.setInt(2, proveedorId);

            stmt.executeUpdate();
            System.out.println("Proveedor asignado al show con éxito.");

        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("Ya existe un registro con estos mismos datos. Verificá que no sea un duplicado.");
            } else {
                System.out.println("El ID ingresado no corresponde a un registro existente. Revisá la lista de opciones mostrada arriba.");
            }
        } catch (SQLException e) {
            System.out.println("Error al asignar el proveedor al show: " + e.getMessage());
        }
    }

    // Consultar los IDs de proveedores asignados a un show
    public List<Integer> consultarPorShow(int showId) {
        List<Integer> proveedorIds = new ArrayList<>();
        String sql = "SELECT proveedor_id FROM show_proveedor WHERE show_id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, showId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    proveedorIds.add(rs.getInt("proveedor_id"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar los proveedores del show: " + e.getMessage());
        }

        return proveedorIds;
    }

    // Eliminar la asignacion de un proveedor a un show
    public void eliminar(int showId, int proveedorId) {
        String sql = "DELETE FROM show_proveedor WHERE show_id = ? AND proveedor_id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, showId);
            stmt.setInt(2, proveedorId);

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Asignación eliminada con éxito.");
            } else {
                System.out.println("No se encontró esa asignación de proveedor y show.");
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar la asignación: " + e.getMessage());
        }
    }
}
