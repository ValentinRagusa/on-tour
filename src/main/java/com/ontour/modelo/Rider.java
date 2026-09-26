package com.ontour.modelo;

import java.time.LocalDate;

public class Rider {

    private int id;
    private int showId;
    private String tipo; // 'tecnico' o 'hospitality'
    private LocalDate fechaCarga;
    private String estado; // 'pendiente', 'en_negociacion' o 'confirmado'

    public Rider(int showId, String tipo, LocalDate fechaCarga, String estado) {
        this.showId = showId;
        this.tipo = tipo;
        this.fechaCarga = fechaCarga;
        this.estado = estado;
    }

    // overload del metodo
    public Rider(int id, int showId, String tipo, LocalDate fechaCarga, String estado) {
        this(showId, tipo, fechaCarga, estado);
        this.id = id;
    }

    // getters
    public int getId() { return id; }
    public int getShowId() { return showId; }
    public String getTipo() { return tipo; }
    public LocalDate getFechaCarga() { return fechaCarga; }
    public String getEstado() { return estado; }

    // setters
    public void setShowId(int showId) { this.showId = showId; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setFechaCarga(LocalDate fechaCarga) { this.fechaCarga = fechaCarga; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Id: " + id +
                " | Show ID: " + showId +
                " | Tipo: " + tipo +
                " | Fecha de carga: " + fechaCarga +
                " | Estado: " + estado;
    }
}
