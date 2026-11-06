package com.ontour.modelo;

import java.time.LocalDate;

// Clase abstracta: no se puede instanciar un Rider "generico", solo sus subclases
// (RiderTecnico o RiderHospitality). Contiene los atributos y comportamientos comunes.
public abstract class Rider {

    private int id;
    private int showId;
    private LocalDate fechaCarga;
    private String estado; // 'pendiente', 'en_negociacion' o 'confirmado'

    // Constructor protected: solo lo pueden invocar las subclases mediante super(...)
    protected Rider(int showId, LocalDate fechaCarga, String estado) {
        this.showId = showId;
        this.fechaCarga = fechaCarga;
        this.estado = estado;
    }

    // overload del metodo
    protected Rider(int id, int showId, LocalDate fechaCarga, String estado) {
        this(showId, fechaCarga, estado);
        this.id = id;
    }

    // Metodos abstractos: cada subclase define su propia implementacion
    // Devuelve el valor del tipo tal como se guarda en la base ('tecnico' o 'hospitality')
    public abstract String getTipo();

    // Describe quien es responsable de resolver este tipo de rider en el negocio
    public abstract String describirResponsable();

    // getters
    public int getId() { return id; }
    public int getShowId() { return showId; }
    public LocalDate getFechaCarga() { return fechaCarga; }
    public String getEstado() { return estado; }

    // setters
    public void setShowId(int showId) { this.showId = showId; }
    public void setFechaCarga(LocalDate fechaCarga) { this.fechaCarga = fechaCarga; }
    public void setEstado(String estado) { this.estado = estado; }

    // getTipo() y describirResponsable() son abstractos: al imprimir un Rider se ejecuta
    // la version de RiderTecnico o RiderHospitality segun el objeto real (polimorfismo dinamico)
    @Override
    public String toString() {
        return "Id: " + id +
                " | Show ID: " + showId +
                " | Tipo: " + getTipo() +
                " | Fecha de carga: " + fechaCarga +
                " | Estado: " + estado +
                "\n   Responsable: " + describirResponsable();
    }
}
