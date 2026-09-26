package com.ontour.dao;

import com.ontour.conexion.ConexionBD;
import com.ontour.modelo.Viatico;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Data Acces Object class
public class ViaticoDAO {
    // Insertar nuevo viatico
    public void insertar(Viatico viatico) {
        String sql = "INSERT INTO viaticos (show_id, integrante_id, monto, fecha, pagado) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, viatico.getShowId());
            stmt.setInt(2, viatico.getIntegranteId());
            stmt.setBigDecimal(3, viatico.getMonto());
            stmt.setDate(4, Date.valueOf(viatico.getFecha()));
            stmt.setBoolean(5, viatico.isPagado());

            stmt.executeUpdate();
            System.out.println("Viático registrado con éxito.");

        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("Ya existe un registro con estos mismos datos. Verificá que no sea un duplicado.");
            } else {
                System.out.println("El ID ingresado no corresponde a un registro existente. Revisá la lista de opciones mostrada arriba.");
            }
        } catch (SQLException e) {
            System.out.println("Error al registrar el viático: " + e.getMessage());
        }
    }

    // Consultar todos los viaticos
    public List<Viatico> consultarTodos() {
        List<Viatico> viaticos = new ArrayList<>();
        String sql = "SELECT * FROM viaticos ORDER BY id";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                viaticos.add(mapearViatico(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar los viáticos: " + e.getMessage());
        }

        return viaticos;
    }

    // Modificar un viatico existente
    public void modificar(Viatico viatico) {
        String sql = "UPDATE viaticos SET show_id = ?, integrante_id = ?, monto = ?, fecha = ?, pagado = ? " +
                "WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, viatico.getShowId());
            stmt.setInt(2, viatico.getIntegranteId());
            stmt.setBigDecimal(3, viatico.getMonto());
            stmt.setDate(4, Date.valueOf(viatico.getFecha()));
            stmt.setBoolean(5, viatico.isPagado());
            stmt.setInt(6, viatico.getId());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Viático modificado con éxito.");
            } else {
                System.out.println("No se encontró un viático con ese ID.");
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("Ya existe un registro con estos mismos datos. Verificá que no sea un duplicado.");
            } else {
                System.out.println("El ID ingresado no corresponde a un registro existente. Revisá la lista de opciones mostrada arriba.");
            }
        } catch (SQLException e) {
            System.out.println("Error al modificar el viático: " + e.getMessage());
        }
    }

    // Eliminar un viatico por ID
    public void eliminar(int id) {
        String sql = "DELETE FROM viaticos WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Viático eliminado con éxito.");
            } else {
                System.out.println("No se encontró un viático con ese ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar el viático: " + e.getMessage());
        }
    }

    // Metodo auxiliar para convertir una fila del ResultSet en un objeto Viatico
    private Viatico mapearViatico(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int showId = rs.getInt("show_id");
        int integranteId = rs.getInt("integrante_id");
        java.math.BigDecimal monto = rs.getBigDecimal("monto");
        LocalDate fecha = rs.getDate("fecha").toLocalDate();
        boolean pagado = rs.getBoolean("pagado");

        return new Viatico(id, showId, integranteId, monto, fecha, pagado);
    }
}
