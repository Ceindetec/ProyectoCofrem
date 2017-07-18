package com.cofrem.transacciones.models;

import java.sql.Timestamp;

public class Transaccion {

    /**
     * Modelo para el registro del Establecimiento
     */
    private int id;
    private int producto_id;
    private int numero_cargo;
    private String numero_tarjeta;
    private String numero_documento;
    private int valor;
    private String registro;
    private int estado;

    /**
     * Constructor vacio de la clase
     */
    public Transaccion() {

    }

    /**
     * Constructor de la clase
     */
    public Transaccion(int id, int producto_id, int numero_cargo, String numero_tarjeta, String numero_documento, int valor, String registro, int estado) {
        this.id = id;
        this.producto_id = producto_id;
        this.numero_cargo = numero_cargo;
        this.numero_tarjeta = numero_tarjeta;
        this.numero_documento = numero_documento;
        this.valor = valor;
        this.registro = registro;
        this.estado = estado;
    }

    /**
     * Getters y Setters
     */


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProducto_id() {
        return producto_id;
    }

    public void setProducto_id(int producto_id) {
        this.producto_id = producto_id;
    }

    public int getNumero_cargo() {
        return numero_cargo;
    }

    public void setNumero_cargo(int numero_cargo) {
        this.numero_cargo = numero_cargo;
    }

    public String getNumero_tarjeta() {
        return numero_tarjeta;
    }

    public void setNumero_tarjeta(String numero_tarjeta) {
        this.numero_tarjeta = numero_tarjeta;
    }

    public String getNumero_documento() {
        return numero_documento;
    }

    public void setNumero_documento(String numero_documento) {
        this.numero_documento = numero_documento;
    }


    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
