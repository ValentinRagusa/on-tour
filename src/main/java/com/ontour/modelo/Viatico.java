package com.ontour.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Viatico {

    private int id;
    private int showId;
    private int integranteId;
    private BigDecimal monto;
    private LocalDate fecha;
    private boolean pagado;

    public Viatico(int showId, int integranteId, BigDecimal monto, LocalDate fecha, boolean pagado) {
        this.showId = showId;
        this.integranteId = integranteId;
        this.monto = monto;
        this.fecha = fecha;
        this.pagado = pagado;
    }

    // overload del metodo
    public Viatico(int id, int showId, int integranteId, BigDecimal monto, LocalDate fecha, boolean pagado) {
        this(showId, integranteId, monto, fecha, pagado);
        this.id = id;
    }

    // getters
    public int getId() { return id; }
    public int getShowId() { return showId; }
    public int getIntegranteId() { return integranteId; }
    public BigDecimal getMonto() { return monto; }
    public LocalDate getFecha() { return fecha; }
    public boolean isPagado() { return pagado; }

    // setters
    public void setShowId(int showId) { this.showId = showId; }
    public void setIntegranteId(int integranteId) { this.integranteId = integranteId; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public void setPagado(boolean pagado) { this.pagado = pagado; }

    @Override
    public String toString() {
        return "Id: " + id +
                " | Show ID: " + showId +
                " | Integrante ID: " + integranteId +
                " | Monto: " + monto +
                " | Fecha: " + fecha +
                " | Pagado: " + (pagado ? "Sí" : "No");
    }
}
