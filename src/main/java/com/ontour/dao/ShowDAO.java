package com.ontour.dao;

import com.ontour.conexion.ConexionBD;
import com.ontour.modelo.Show;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

// Data Acces Object class
public class ShowDAO {
    // Insertar nuevo show
    public void insertar(Show show) {
        String sql = "INSERT INTO shows (nombre_venue, ciudad, pais, fecha, hora_llegada, hora_soundcheck, hora_show) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, show.getNombreVenue());
            stmt.setString(2, show.getCiudad());
            stmt.setString(3, show.getPais());
            stmt.setDate(4, Date.valueOf(show.getFecha()));
            stmt.setTime(5, show.getHoraLlegada() != null ? Time.valueOf(show.getHoraLlegada()) : null);
            stmt.setTime(6, show.getHoraSoundcheck() != null ? Time.valueOf(show.getHoraSoundcheck()) : null);
            stmt.setTime(7, show.getHoraShow() != null ? Time.valueOf(show.getHoraShow()) : null);

            stmt.executeUpdate();
            System.out.println("Show registrado con éxito.");

        } catch (SQLException e) {
            System.out.println("Error al registrar el show: " + e.getMessage());
        }
    }

    // Consultar todos los shows
    public List<Show> consultarTodos() {
        List<Show> shows = new ArrayList<>();
        String sql = "SELECT * FROM shows ORDER BY fecha";

        try (Connection conexion = ConexionBD.obtenerConexion();
        PreparedStatement stmt = conexion.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                shows.add(mapearShow(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar los shows: " + e.getMessage());
        }

        return shows;
    }

    // Modificar un show existente
    public void modificar(Show show) {
        String sql = "UPDATE shows SET nombre_venue = ?, ciudad = ?, pais = ?, fecha = ?, " +
                "hora_llegada = ?, hora_soundcheck = ?, hora_show = ? WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
        PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, show.getNombreVenue());
            stmt.setString(2, show.getCiudad());
            stmt.setString(3, show.getPais());
            stmt.setDate(4, Date.valueOf(show.getFecha()));
            stmt.setTime(5, show.getHoraLlegada() != null ? Time.valueOf(show.getHoraLlegada()) : null);
            stmt.setTime(6, show.getHoraSoundcheck() != null ? Time.valueOf(show.getHoraSoundcheck()) : null);
            stmt.setTime(7, show.getHoraShow() != null ? Time.valueOf(show.getHoraShow()) : null);
            stmt.setInt(8, show.getId());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Show modificado con éxito.");
            } else {
                System.out.println("No se encontró un show con ese ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error al modificar el show: " + e.getMessage());
        }
    }

    // Eliminar un show por ID
    public void eliminar(int id) {
        String sql = "DELETE FROM shows WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Show eliminado con exito.");
            } else {
                System.out.println("No se encontró un show con ese ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error al modificar el show: " + e.getMessage());
        }
    }

    // Metodo auxiliar para convertir una fila del ResultSet en un objeto Show
    // esto es para no repetir el codigo de 'leer una fila y convertirla en objeto Show' en varios lugares.
    private Show mapearShow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nombreVenue = rs.getString("nombre_venue");
        String ciudad = rs.getString("ciudad");
        String pais = rs.getString("pais");
        LocalDate fecha = rs.getDate("fecha").toLocalDate();

        Time llegadaSql = rs.getTime("hora_llegada");
        LocalTime horaLlegada = llegadaSql != null ? llegadaSql.toLocalTime() : null;

        Time soundcheckSql = rs.getTime("hora_llegada");
        LocalTime horaSoundcheck = soundcheckSql != null ? soundcheckSql.toLocalTime() : null;

        Time showSql = rs.getTime("hora_show");
        LocalTime horaShow = showSql != null ? showSql.toLocalTime() : null;

        return new Show(id,nombreVenue, ciudad, pais, fecha, horaLlegada, horaSoundcheck, horaShow);
    }
}
