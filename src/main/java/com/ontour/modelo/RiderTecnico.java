package com.ontour.modelo;

import java.time.LocalDate;

// Rider tecnico: requerimientos de sonido, iluminacion y escenario
public class RiderTecnico extends Rider {

    public RiderTecnico(int showId, LocalDate fechaCarga, String estado) {
        super(showId, fechaCarga, estado);
    }

    // overload del metodo
    public RiderTecnico(int id, int showId, LocalDate fechaCarga, String estado) {
        super(id, showId, fechaCarga, estado);
    }

    @Override
    public String getTipo() {
        return "tecnico";
    }

    @Override
    public String describirResponsable() {
        return "Jefe de Stage, Sonido e Iluminación";
    }
}
