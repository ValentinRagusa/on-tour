package com.ontour.modelo;

public class Habitacion {

    private int id;
    private int showId;
    private String tipo; // 'individual', 'doble' o 'triple'
    private String numeroHabitacion;

    public Habitacion(int showId, String tipo, String numeroHabitacion) {
        this.showId = showId;
        this.tipo = tipo;
        this.numeroHabitacion = numeroHabitacion;
    }

    // overload del metodo
    public Habitacion(int id, int showId, String tipo, String numeroHabitacion) {
        this(showId, tipo, numeroHabitacion);
        this.id = id;
    }

    // getters
    public int getId() { return id; }
    public int getShowId() { return showId; }
    public String getTipo() { return tipo; }
    public String getNumeroHabitacion() { return numeroHabitacion; }

    // setters
    public void setShowId(int showId) { this.showId = showId; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setNumeroHabitacion(String numeroHabitacion) { this.numeroHabitacion = numeroHabitacion; }

    @Override
    public String toString() {
        return "Id: " + id +
                " | Show ID: " + showId +
                " | Tipo: " + tipo +
                " | Número: " + numeroHabitacion;
    }
}
