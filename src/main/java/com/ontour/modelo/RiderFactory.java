package com.ontour.modelo;

import com.ontour.excepcion.OnTourException;

import java.time.LocalDate;

// Patron Simple Factory: aca se decide que subclase de Rider crear segun el tipo.
// Antes este switch estaba repetido en Main y en RiderDAO. Ahora el resto del codigo
// solo pide un Rider y no necesita conocer RiderTecnico ni RiderHospitality.
// Si mas adelante se agrega otro tipo de rider, se toca solamente esta clase.
public class RiderFactory {

    // Constructor privado: la clase solo se usa a traves de su metodo estatico
    private RiderFactory() {
    }

    public static Rider crear(int id, int showId, String tipo, LocalDate fechaCarga, String estado) {
        return switch (tipo) {
            case "tecnico" -> new RiderTecnico(id, showId, fechaCarga, estado);
            case "hospitality" -> new RiderHospitality(id, showId, fechaCarga, estado);
            default -> throw new OnTourException("Tipo de rider desconocido: " + tipo);
        };
    }
}
