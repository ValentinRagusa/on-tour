package com.ontour.dao;

import com.ontour.conexion.ConexionBD;
import com.ontour.excepcion.OnTourException;
import com.ontour.modelo.Viatico;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Data Acces Object class
public class ViaticoDAO implements CrudDAO<Viatico> {
    // Insertar nuevo viatico
    @Override
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
    @Override
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
    @Override
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
    @Override
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

    // Muestra un resumen liviano (id + integrante + monto) para elegir un viatico al completar otro formulario
    @Override
    public void mostrarResumen() {
        String sql = "SELECT id, integrante_id, monto, pagado FROM viaticos ORDER BY id";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + " | Integrante ID: " + rs.getInt("integrante_id") +
                        " | Monto: " + rs.getBigDecimal("monto") +
                        " | Pagado: " + (rs.getBoolean("pagado") ? "Sí" : "No"));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar el resumen de viáticos: " + e.getMessage());
        }
    }

    // Marcar un viatico como pagado. Si ya estaba pagado, lanza OnTourException (regla de negocio)
    public void marcarComoPagado(int id) {
        String sqlConsulta = "SELECT pagado FROM viaticos WHERE id = ?";
        String sqlUpdate = "UPDATE viaticos SET pagado = TRUE WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmtConsulta = conexion.prepareStatement(sqlConsulta)) {

            stmtConsulta.setInt(1, id);

            try (ResultSet rs = stmtConsulta.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("No se encontró un viático con ese ID.");
                    return;
                }
                if (rs.getBoolean("pagado")) {
                    throw new OnTourException("Este viático ya fue marcado como pagado anteriormente.");
                }
            }

            try (PreparedStatement stmtUpdate = conexion.prepareStatement(sqlUpdate)) {
                stmtUpdate.setInt(1, id);
                stmtUpdate.executeUpdate();
                System.out.println("Viático marcado como pagado con éxito.");
            }

        } catch (SQLException e) {
            System.out.println("Error al marcar el viático como pagado: " + e.getMessage());
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
