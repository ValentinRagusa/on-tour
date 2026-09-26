package com.ontour.dao;

import com.ontour.conexion.ConexionBD;
import com.ontour.modelo.Proveedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Data Acces Object class
public class ProveedorDAO {
    // Insertar nuevo proveedor
    public void insertar(Proveedor proveedor) {
        String sql = "INSERT INTO proveedores (nombre, tipo, contacto) VALUES (?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, proveedor.getNombre());
            stmt.setString(2, proveedor.getTipo());
            stmt.setString(3, proveedor.getContacto());

            stmt.executeUpdate();
            System.out.println("Proveedor registrado con éxito.");

        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("Ya existe un registro con estos mismos datos. Verificá que no sea un duplicado.");
            } else {
                System.out.println("El ID ingresado no corresponde a un registro existente. Revisá la lista de opciones mostrada arriba.");
            }
        } catch (SQLException e) {
            System.out.println("Error al registrar el proveedor: " + e.getMessage());
        }
    }

    // Consultar todos los proveedores
    public List<Proveedor> consultarTodos() {
        List<Proveedor> proveedores = new ArrayList<>();
        String sql = "SELECT * FROM proveedores ORDER BY id";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                proveedores.add(mapearProveedor(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar los proveedores: " + e.getMessage());
        }

        return proveedores;
    }

    // Modificar un proveedor existente
    public void modificar(Proveedor proveedor) {
        String sql = "UPDATE proveedores SET nombre = ?, tipo = ?, contacto = ? WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, proveedor.getNombre());
            stmt.setString(2, proveedor.getTipo());
            stmt.setString(3, proveedor.getContacto());
            stmt.setInt(4, proveedor.getId());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Proveedor modificado con éxito.");
            } else {
                System.out.println("No se encontró un proveedor con ese ID.");
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("Ya existe un registro con estos mismos datos. Verificá que no sea un duplicado.");
            } else {
                System.out.println("El ID ingresado no corresponde a un registro existente. Revisá la lista de opciones mostrada arriba.");
            }
        } catch (SQLException e) {
            System.out.println("Error al modificar el proveedor: " + e.getMessage());
        }
    }

    // Eliminar un proveedor por ID
    public void eliminar(int id) {
        String sql = "DELETE FROM proveedores WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Proveedor eliminado con éxito.");
            } else {
                System.out.println("No se encontró un proveedor con ese ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar el proveedor: " + e.getMessage());
        }
    }

    // Muestra un resumen liviano (id + nombre) para elegir un proveedor al completar otro formulario
    public void mostrarResumen() {
        String sql = "SELECT id, nombre FROM proveedores ORDER BY id";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + " | Nombre: " + rs.getString("nombre"));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar el resumen de proveedores: " + e.getMessage());
        }
    }

    // Metodo auxiliar para convertir una fila del ResultSet en un objeto Proveedor
    private Proveedor mapearProveedor(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nombre = rs.getString("nombre");
        String tipo = rs.getString("tipo");
        String contacto = rs.getString("contacto");

        return new Proveedor(id, nombre, tipo, contacto);
    }
}
