package com.JDomingoCajeroAutomatico.JDomingoCajeroAutomatico.JPA;

import java.util.List;

public class Result {

    public boolean correct;
    public String errorMessage; // descripción del error del mensaje
    public Exception ex; // excepciones
    public Object object; // int, string, alumno, lista<Alumno>, ARREGLO, MATRIZ 
    public List<Object> objects;
    public List<String> denominationDetails; // Lista de detalles de los billetes entregados
}
