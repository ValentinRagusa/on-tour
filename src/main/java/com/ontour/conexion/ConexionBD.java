package com.ontour.conexion;

import com.ontour.excepcion.OnTourException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionBD {

    // Los datos de conexion ya no estan escritos en el codigo: se leen de
    // src/main/resources/db.properties. Asi se pueden cambiar sin recompilar.
    private static final String ARCHIVO_CONFIGURACION = "db.properties";

    // Se carga una sola vez, la primera vez que se pide una conexion
    private static Properties configuracion;

    public static Connection obtenerConexion() throws SQLException {
        Properties config = cargarConfiguracion();
        return DriverManager.getConnection(
                config.getProperty("db.url"),
                config.getProperty("db.usuario"),
                config.getProperty("db.password"));
    }

    private static Properties cargarConfiguracion() {
        if (configuracion != null) {
            return configuracion;
        }

        // getResourceAsStream busca el archivo dentro del classpath (la carpeta resources)
        try (InputStream entrada = ConexionBD.class.getClassLoader().getResourceAsStream(ARCHIVO_CONFIGURACION)) {
            if (entrada == null) {
                throw new OnTourException("No se encontró el archivo " + ARCHIVO_CONFIGURACION
                        + " en src/main/resources.");
            }

            Properties props = new Properties();
            props.load(entrada);

            if (props.getProperty("db.url") == null || props.getProperty("db.usuario") == null) {
                throw new OnTourException("Al archivo " + ARCHIVO_CONFIGURACION
                        + " le falta db.url o db.usuario.");
            }

            configuracion = props;
            return configuracion;

        } catch (IOException e) {
            throw new OnTourException("Error al leer " + ARCHIVO_CONFIGURACION + ": " + e.getMessage());
        }
    }
}
