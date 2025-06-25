package com.JDomingoCajeroAutomatico.JDomingoCajeroAutomatico.JPA;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
public class Denominacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    @Column(name = "iddenominacion")
    private int IdDenominacion;
    
    @JoinColumn(name = "idmoneda")
    @ManyToOne
    public Moneda moneda;
    
    @Column(name = "denominacion")
    private BigDecimal Denominacion;

    public int getIdDenominacion() {
        return IdDenominacion;
    }

    public void setIdDenominacion(int IdDenominacion) {
        this.IdDenominacion = IdDenominacion;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public BigDecimal getDenominacion() {
        return Denominacion;
    }

    public void setDenominacion(BigDecimal Denominacion) {
        this.Denominacion = Denominacion;
    }
    
    
    
}
