package com.ontour.modelo;

public class Proveedor {

    private int id;
    private String nombre;
    private String tipo; // 'transporte', 'alojamiento' o 'tecnico'
    private String contacto;

    public Proveedor(String nombre, String tipo, String contacto) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.contacto = contacto;
    }

    // overload del metodo
    public Proveedor(int id, String nombre, String tipo, String contacto) {
        this(nombre, tipo, contacto);
        this.id = id;
    }

    // getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public String getContacto() { return contacto; }

    // setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setContacto(String contacto) { this.contacto = contacto; }

    @Override
    public String toString() {
        return "Id: " + id +
                " | Nombre: " + nombre +
                " | Tipo: " + tipo +
                " | Contacto: " + contacto;
    }
}
