package com.ontour.modelo;

import java.time.LocalDate;

// Rider de hospitality: catering, camarines, alojamiento y atencion a la banda
public class RiderHospitality extends Rider {

    public RiderHospitality(int showId, LocalDate fechaCarga, String estado) {
        super(showId, fechaCarga, estado);
    }

    // overload del metodo
    public RiderHospitality(int id, int showId, LocalDate fechaCarga, String estado) {
        super(id, showId, fechaCarga, estado);
    }

    @Override
    public String getTipo() {
        return "hospitality";
    }

    @Override
    public String describirResponsable() {
        return "Tour Manager y Manager";
    }
}
