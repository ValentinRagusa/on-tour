package com.ontour.dao;

import com.ontour.conexion.ConexionBD;
import com.ontour.modelo.DocumentoIntegrante;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Data Acces Object class
public class DocumentoIntegranteDAO {
    // Insertar nuevo documento
    public void insertar(DocumentoIntegrante documento) {
        String sql = "INSERT INTO documentos_integrante (integrante_id, tipo_documento, archivo, fecha_carga) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, documento.getIntegranteId());
            stmt.setString(2, documento.getTipoDocumento());
            stmt.setString(3, documento.getArchivo());
            stmt.setDate(4, Date.valueOf(documento.getFechaCarga()));

            stmt.executeUpdate();
            System.out.println("Documento registrado con éxito.");

        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("Ya existe un registro con estos mismos datos. Verificá que no sea un duplicado.");
            } else {
                System.out.println("El ID ingresado no corresponde a un registro existente. Revisá la lista de opciones mostrada arriba.");
            }
        } catch (SQLException e) {
            System.out.println("Error al registrar el documento: " + e.getMessage());
        }
    }

    // Consultar todos los documentos
    public List<DocumentoIntegrante> consultarTodos() {
        List<DocumentoIntegrante> documentos = new ArrayList<>();
        String sql = "SELECT * FROM documentos_integrante ORDER BY id";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                documentos.add(mapearDocumento(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar los documentos: " + e.getMessage());
        }

        return documentos;
    }

    // Modificar un documento existente
    public void modificar(DocumentoIntegrante documento) {
        String sql = "UPDATE documentos_integrante SET integrante_id = ?, tipo_documento = ?, archivo = ?, " +
                "fecha_carga = ? WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, documento.getIntegranteId());
            stmt.setString(2, documento.getTipoDocumento());
            stmt.setString(3, documento.getArchivo());
            stmt.setDate(4, Date.valueOf(documento.getFechaCarga()));
            stmt.setInt(5, documento.getId());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Documento modificado con éxito.");
            } else {
                System.out.println("No se encontró un documento con ese ID.");
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("Ya existe un registro con estos mismos datos. Verificá que no sea un duplicado.");
            } else {
                System.out.println("El ID ingresado no corresponde a un registro existente. Revisá la lista de opciones mostrada arriba.");
            }
        } catch (SQLException e) {
            System.out.println("Error al modificar el documento: " + e.getMessage());
        }
    }

    // Eliminar un documento por ID
    public void eliminar(int id) {
        String sql = "DELETE FROM documentos_integrante WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Documento eliminado con éxito.");
            } else {
                System.out.println("No se encontró un documento con ese ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar el documento: " + e.getMessage());
        }
    }

    // Metodo auxiliar para convertir una fila del ResultSet en un objeto DocumentoIntegrante
    private DocumentoIntegrante mapearDocumento(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int integranteId = rs.getInt("integrante_id");
        String tipoDocumento = rs.getString("tipo_documento");
        String archivo = rs.getString("archivo");
        LocalDate fechaCarga = rs.getDate("fecha_carga").toLocalDate();

        return new DocumentoIntegrante(id, integranteId, tipoDocumento, archivo, fechaCarga);
    }
}
