package com.ontour.dao;

import com.ontour.conexion.ConexionBD;
import com.ontour.modelo.Contrarider;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Data Acces Object class
public class ContrariderDAO {
    // Insertar nuevo contrarider
    public void insertar(Contrarider contrarider) {
        String sql = "INSERT INTO contrariders (rider_id, version, fecha_carga, descripcion, disponibilidad) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, contrarider.getRiderId());
            stmt.setInt(2, contrarider.getVersion());
            stmt.setDate(3, Date.valueOf(contrarider.getFechaCarga()));
            stmt.setString(4, contrarider.getDescripcion());
            stmt.setString(5, contrarider.getDisponibilidad());

            stmt.executeUpdate();
            System.out.println("Contrarider registrado con éxito.");

        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("Ya existe un registro con estos mismos datos. Verificá que no sea un duplicado.");
            } else {
                System.out.println("El ID ingresado no corresponde a un registro existente. Revisá la lista de opciones mostrada arriba.");
            }
        } catch (SQLException e) {
            System.out.println("Error al registrar el contrarider: " + e.getMessage());
        }
    }

    // Consultar todos los contrariders
    public List<Contrarider> consultarTodos() {
        List<Contrarider> contrariders = new ArrayList<>();
        String sql = "SELECT * FROM contrariders ORDER BY id";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                contrariders.add(mapearContrarider(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar los contrariders: " + e.getMessage());
        }

        return contrariders;
    }

    // Modificar un contrarider existente
    public void modificar(Contrarider contrarider) {
        String sql = "UPDATE contrariders SET rider_id = ?, version = ?, fecha_carga = ?, descripcion = ?, " +
                "disponibilidad = ? WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, contrarider.getRiderId());
            stmt.setInt(2, contrarider.getVersion());
            stmt.setDate(3, Date.valueOf(contrarider.getFechaCarga()));
            stmt.setString(4, contrarider.getDescripcion());
            stmt.setString(5, contrarider.getDisponibilidad());
            stmt.setInt(6, contrarider.getId());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Contrarider modificado con éxito.");
            } else {
                System.out.println("No se encontró un contrarider con ese ID.");
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("Ya existe un registro con estos mismos datos. Verificá que no sea un duplicado.");
            } else {
                System.out.println("El ID ingresado no corresponde a un registro existente. Revisá la lista de opciones mostrada arriba.");
            }
        } catch (SQLException e) {
            System.out.println("Error al modificar el contrarider: " + e.getMessage());
        }
    }

    // Eliminar un contrarider por ID
    public void eliminar(int id) {
        String sql = "DELETE FROM contrariders WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Contrarider eliminado con éxito.");
            } else {
                System.out.println("No se encontró un contrarider con ese ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar el contrarider: " + e.getMessage());
        }
    }

    // Metodo auxiliar para convertir una fila del ResultSet en un objeto Contrarider
    private Contrarider mapearContrarider(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int riderId = rs.getInt("rider_id");
        int version = rs.getInt("version");
        LocalDate fechaCarga = rs.getDate("fecha_carga").toLocalDate();
        String descripcion = rs.getString("descripcion");
        String disponibilidad = rs.getString("disponibilidad");

        return new Contrarider(id, riderId, version, fechaCarga, descripcion, disponibilidad);
    }
}
