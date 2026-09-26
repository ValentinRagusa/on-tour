package com.ontour.modelo;

import java.time.LocalDate;

public class DocumentoIntegrante {

    private int id;
    private int integranteId;
    private String tipoDocumento;
    private String archivo;
    private LocalDate fechaCarga;

    public DocumentoIntegrante(int integranteId, String tipoDocumento, String archivo, LocalDate fechaCarga) {
        this.integranteId = integranteId;
        this.tipoDocumento = tipoDocumento;
        this.archivo = archivo;
        this.fechaCarga = fechaCarga;
    }

    // overload del metodo
    public DocumentoIntegrante(int id, int integranteId, String tipoDocumento, String archivo, LocalDate fechaCarga) {
        this(integranteId, tipoDocumento, archivo, fechaCarga);
        this.id = id;
    }

    // getters
    public int getId() { return id; }
    public int getIntegranteId() { return integranteId; }
    public String getTipoDocumento() { return tipoDocumento; }
    public String getArchivo() { return archivo; }
    public LocalDate getFechaCarga() { return fechaCarga; }

    // setters
    public void setIntegranteId(int integranteId) { this.integranteId = integranteId; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }
    public void setArchivo(String archivo) { this.archivo = archivo; }
    public void setFechaCarga(LocalDate fechaCarga) { this.fechaCarga = fechaCarga; }

    @Override
    public String toString() {
        return "Id: " + id +
                " | Integrante ID: " + integranteId +
                " | Tipo de documento: " + tipoDocumento +
                " | Archivo: " + archivo +
                " | Fecha de carga: " + fechaCarga;
    }
}
