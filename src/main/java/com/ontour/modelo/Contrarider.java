package com.ontour.modelo;

import java.time.LocalDate;

public class Contrarider {

    private int id;
    private int riderId;
    private int version;
    private LocalDate fechaCarga;
    private String descripcion;
    private String disponibilidad; // 'confirmado', 'alternativa' o 'no_disponible'

    public Contrarider(int riderId, int version, LocalDate fechaCarga, String descripcion, String disponibilidad) {
        this.riderId = riderId;
        this.version = version;
        this.fechaCarga = fechaCarga;
        this.descripcion = descripcion;
        this.disponibilidad = disponibilidad;
    }

    // overload del metodo
    public Contrarider(int id, int riderId, int version, LocalDate fechaCarga, String descripcion, String disponibilidad) {
        this(riderId, version, fechaCarga, descripcion, disponibilidad);
        this.id = id;
    }

    // getters
    public int getId() { return id; }
    public int getRiderId() { return riderId; }
    public int getVersion() { return version; }
    public LocalDate getFechaCarga() { return fechaCarga; }
    public String getDescripcion() { return descripcion; }
    public String getDisponibilidad() { return disponibilidad; }

    // setters
    public void setRiderId(int riderId) { this.riderId = riderId; }
    public void setVersion(int version) { this.version = version; }
    public void setFechaCarga(LocalDate fechaCarga) { this.fechaCarga = fechaCarga; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setDisponibilidad(String disponibilidad) { this.disponibilidad = disponibilidad; }

    @Override
    public String toString() {
        return "Id: " + id +
                " | Rider ID: " + riderId +
                " | Versión: " + version +
                " | Fecha de carga: " + fechaCarga +
                " | Descripción: " + descripcion +
                " | Disponibilidad: " + disponibilidad;
    }
}
