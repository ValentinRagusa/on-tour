package com.ontour.dao;

import com.ontour.conexion.ConexionBD;
import com.ontour.modelo.Rider;
import com.ontour.modelo.RiderHospitality;
import com.ontour.modelo.RiderTecnico;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Data Acces Object class
public class RiderDAO implements CrudDAO<Rider> {
    // Insertar nuevo rider
    @Override
    public void insertar(Rider rider) {
        String sql = "INSERT INTO riders (show_id, tipo, fecha_carga, estado) VALUES (?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, rider.getShowId());
            stmt.setString(2, rider.getTipo());
            stmt.setDate(3, Date.valueOf(rider.getFechaCarga()));
            stmt.setString(4, rider.getEstado());

            stmt.executeUpdate();
            System.out.println("Rider registrado con éxito.");

        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("Ya existe un registro con estos mismos datos. Verificá que no sea un duplicado.");
            } else {
                System.out.println("El ID ingresado no corresponde a un registro existente. Revisá la lista de opciones mostrada arriba.");
            }
        } catch (SQLException e) {
            System.out.println("Error al registrar el rider: " + e.getMessage());
        }
    }

    // Consultar todos los riders
    @Override
    public List<Rider> consultarTodos() {
        List<Rider> riders = new ArrayList<>();
        String sql = "SELECT * FROM riders ORDER BY id";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                riders.add(mapearRider(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar los riders: " + e.getMessage());
        }

        return riders;
    }

    // Modificar un rider existente
    @Override
    public void modificar(Rider rider) {
        String sql = "UPDATE riders SET show_id = ?, tipo = ?, fecha_carga = ?, estado = ? WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, rider.getShowId());
            stmt.setString(2, rider.getTipo());
            stmt.setDate(3, Date.valueOf(rider.getFechaCarga()));
            stmt.setString(4, rider.getEstado());
            stmt.setInt(5, rider.getId());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Rider modificado con éxito.");
            } else {
                System.out.println("No se encontró un rider con ese ID.");
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("Ya existe un registro con estos mismos datos. Verificá que no sea un duplicado.");
            } else {
                System.out.println("El ID ingresado no corresponde a un registro existente. Revisá la lista de opciones mostrada arriba.");
            }
        } catch (SQLException e) {
            System.out.println("Error al modificar el rider: " + e.getMessage());
        }
    }

    // Eliminar un rider por ID
    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM riders WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Rider eliminado con éxito.");
            } else {
                System.out.println("No se encontró un rider con ese ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar el rider: " + e.getMessage());
        }
    }

    // Muestra un resumen liviano (id + tipo) para elegir un rider al completar otro formulario
    @Override
    public void mostrarResumen() {
        String sql = "SELECT id, tipo FROM riders ORDER BY id";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + " | Tipo: " + rs.getString("tipo"));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar el resumen de riders: " + e.getMessage());
        }
    }

    // Metodo auxiliar para convertir una fila del ResultSet en un objeto Rider.
    // Segun la columna "tipo" devuelve una instancia de la subclase correspondiente,
    // por eso consultarTodos() termina devolviendo una lista con RiderTecnico y RiderHospitality mezclados.
    private Rider mapearRider(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int showId = rs.getInt("show_id");
        String tipo = rs.getString("tipo");
        LocalDate fechaCarga = rs.getDate("fecha_carga").toLocalDate();
        String estado = rs.getString("estado");

        return switch (tipo) {
            case "tecnico" -> new RiderTecnico(id, showId, fechaCarga, estado);
            case "hospitality" -> new RiderHospitality(id, showId, fechaCarga, estado);
            default -> throw new SQLException("Tipo de rider desconocido en la base: " + tipo);
        };
    }
}
