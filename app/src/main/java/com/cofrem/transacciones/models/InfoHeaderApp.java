package com.cofrem.transacciones.models;

public class InfoHeaderApp {

    private static InfoHeaderApp instance = null;
    /**
     * Modelo para el registro del InfoHeaderApp
     */
    private String idDispositivo = "NO REGISTRA";
    private String idPunto = "NO REGISTRA";
    private String nombreEstablecimiento = "NO REGISTRA";
    private String nombrePunto = "NO REGISTRA";

    protected InfoHeaderApp() {
    }

    public static InfoHeaderApp getInstance() {
        if (instance == null) {
            instance = new InfoHeaderApp();
        }
        return instance;
    }

    public String getIdDispositivo() {
        return idDispositivo;
    }

    public void setIdDispositivo(String idDispositivo) {
        this.idDispositivo = idDispositivo;
    }

    public String getIdPunto() {
        return idPunto;
    }

    public void setIdPunto(String idPunto) {
        this.idPunto = idPunto;
    }

    public String getNombreEstablecimiento() {
        return nombreEstablecimiento;
    }

    public void setNombreEstablecimiento(String nombreEstablecimiento) {
        this.nombreEstablecimiento = nombreEstablecimiento;
    }

    public String getNombrePunto() {
        return nombrePunto;
    }

    public void setNombrePunto(String nombrePunto) {
        this.nombrePunto = nombrePunto;
    }
}
