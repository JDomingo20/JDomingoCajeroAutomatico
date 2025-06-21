
package com.JDomingoCajeroAutomatico.JDomingoCajeroAutomatico.JPA;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class CantidadMoneda {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcantidad")
    private int IdCantidad;
    
    @JoinColumn(name = "iddenominacion")
    @ManyToOne
    public Denominacion denominacion;
    
    @Column(name = "cantidad")
    private int Cantidad;

    public int getIdCantidad() {
        return IdCantidad;
    }

    public void setIdCantidad(int IdCantidad) {
        this.IdCantidad = IdCantidad;
    }

    public Denominacion getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(Denominacion denominacion) {
        this.denominacion = denominacion;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int Cantidad) {
        this.Cantidad = Cantidad;
    }
    
    
    
}
