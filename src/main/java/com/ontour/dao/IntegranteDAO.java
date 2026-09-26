package com.ontour.dao;

import com.ontour.conexion.ConexionBD;
import com.ontour.modelo.Integrante;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Data Acces Object class
public class IntegranteDAO {
    // Insertar nuevo integrante
    public void insertar(Integrante integrante) {
        String sql = "INSERT INTO integrantes (nombre, documento_identidad, numero_pasajero_frecuente) " +
                "VALUES (?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, integrante.getNombre());
            stmt.setString(2, integrante.getDocumentoIdentidad());
            stmt.setString(3, integrante.getNumeroPasajeroFrecuente());

            stmt.executeUpdate();
            System.out.println("Integrante registrado con éxito.");

        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("Ya existe un registro con estos mismos datos. Verificá que no sea un duplicado.");
            } else {
                System.out.println("El ID ingresado no corresponde a un registro existente. Revisá la lista de opciones mostrada arriba.");
            }
        } catch (SQLException e) {
            System.out.println("Error al registrar el integrante: " + e.getMessage());
        }
    }

    // Consultar todos los integrantes
    public List<Integrante> consultarTodos() {
        List<Integrante> integrantes = new ArrayList<>();
        String sql = "SELECT * FROM integrantes ORDER BY id";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                integrantes.add(mapearIntegrante(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar los integrantes: " + e.getMessage());
        }

        return integrantes;
    }

    // Modificar un integrante existente
    public void modificar(Integrante integrante) {
        String sql = "UPDATE integrantes SET nombre = ?, documento_identidad = ?, numero_pasajero_frecuente = ? " +
                "WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, integrante.getNombre());
            stmt.setString(2, integrante.getDocumentoIdentidad());
            stmt.setString(3, integrante.getNumeroPasajeroFrecuente());
            stmt.setInt(4, integrante.getId());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Integrante modificado con éxito.");
            } else {
                System.out.println("No se encontró un integrante con ese ID.");
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("Ya existe un registro con estos mismos datos. Verificá que no sea un duplicado.");
            } else {
                System.out.println("El ID ingresado no corresponde a un registro existente. Revisá la lista de opciones mostrada arriba.");
            }
        } catch (SQLException e) {
            System.out.println("Error al modificar el integrante: " + e.getMessage());
        }
    }

    // Eliminar un integrante por ID
    public void eliminar(int id) {
        String sql = "DELETE FROM integrantes WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Integrante eliminado con éxito.");
            } else {
                System.out.println("No se encontró un integrante con ese ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar el integrante: " + e.getMessage());
        }
    }

    // Muestra un resumen liviano (id + nombre) para elegir un integrante al completar otro formulario
    public void mostrarResumen() {
        String sql = "SELECT id, nombre FROM integrantes ORDER BY id";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + " | Nombre: " + rs.getString("nombre"));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar el resumen de integrantes: " + e.getMessage());
        }
    }

    // Metodo auxiliar para convertir una fila del ResultSet en un objeto Integrante
    private Integrante mapearIntegrante(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nombre = rs.getString("nombre");
        String documentoIdentidad = rs.getString("documento_identidad");
        String numeroPasajeroFrecuente = rs.getString("numero_pasajero_frecuente");

        return new Integrante(id, nombre, documentoIdentidad, numeroPasajeroFrecuente);
    }
}
