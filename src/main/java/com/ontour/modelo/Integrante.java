package com.ontour.modelo;

public class Integrante {

    private int id;
    private String nombre;
    private String documentoIdentidad;
    private String numeroPasajeroFrecuente;

    public Integrante(String nombre, String documentoIdentidad, String numeroPasajeroFrecuente) {
        this.nombre = nombre;
        this.documentoIdentidad = documentoIdentidad;
        this.numeroPasajeroFrecuente = numeroPasajeroFrecuente;
    }

    // overload del metodo
    public Integrante(int id, String nombre, String documentoIdentidad, String numeroPasajeroFrecuente) {
        this(nombre, documentoIdentidad, numeroPasajeroFrecuente);
        this.id = id;
    }

    // getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDocumentoIdentidad() { return documentoIdentidad; }
    public String getNumeroPasajeroFrecuente() { return numeroPasajeroFrecuente; }

    // setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDocumentoIdentidad(String documentoIdentidad) { this.documentoIdentidad = documentoIdentidad; }
    public void setNumeroPasajeroFrecuente(String numeroPasajeroFrecuente) { this.numeroPasajeroFrecuente = numeroPasajeroFrecuente; }

    @Override
    public String toString() {
        return "Id: " + id +
                " | Nombre: " + nombre +
                " | Documento: " + documentoIdentidad +
                " | Nro. pasajero frecuente: " + numeroPasajeroFrecuente;
    }
}
