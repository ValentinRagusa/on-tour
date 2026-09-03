package com.ontour.modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Show {

    private int id;
    private String nombreVenue;
    private String ciudad;
    private String pais;
    private LocalDate fecha;
    private LocalTime horaLlegada;
    private LocalTime horaSoundcheck;
    private LocalTime horaShow;

    public Show(String nombreVenue, String ciudad, String pais,
                LocalDate fecha, LocalTime horaLlegada,LocalTime horaSoundcheck,
                LocalTime horaShow) {
        this.nombreVenue = nombreVenue;
        this.ciudad = ciudad;
        this.pais = pais;
        this.fecha = fecha;
        this.horaLlegada = horaLlegada;
        this.horaSoundcheck = horaSoundcheck;
        this.horaShow = horaShow;
    }

    // overload del metodo
    public Show(int id, String nombreVenue, String ciudad, String pais,
                LocalDate fecha, LocalTime horaLlegada,LocalTime horaSoundcheck,
                LocalTime horaShow) {
        this(nombreVenue, ciudad, pais, fecha, horaLlegada, horaSoundcheck, horaShow);
        this.id = id;
    }

    // getters
    public int getId() { return id; }
    public String getNombreVenue() { return nombreVenue; }
    public String getCiudad() { return ciudad; }
    public String getPais() { return pais; }
    public LocalDate getFecha() { return fecha; }
    public LocalTime getHoraLlegada() { return horaLlegada; }
    public LocalTime getHoraSoundcheck() { return horaSoundcheck; }
    public LocalTime getHoraShow() { return horaShow; }

    // setters
    public void setNombreVenue(String nombreVenue) { this.nombreVenue = nombreVenue; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public void setPais(String pais) { this.pais = pais; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public void setHoraLlegada(LocalTime horaLlegada) { this.horaLlegada = horaLlegada; }
    public void setHoraSoundcheck(LocalTime horaSoundcheck) { this.horaSoundcheck = horaSoundcheck; }
    public void setHoraShow(LocalTime horaShow) { this.horaShow = horaShow; }

    @Override
    public String toString() {
        return "Id: " + id +
                " | Venue: " + nombreVenue +
                " | Ciudad: " + ciudad +
                " | Pais: " + pais +
                " | Fecha: " + fecha +
                " | Llegada: " + horaLlegada +
                " | Soundcheck: " + horaSoundcheck +
                " | Show: " + horaShow;
    }
}
