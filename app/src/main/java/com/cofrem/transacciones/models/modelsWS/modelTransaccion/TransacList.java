package com.cofrem.transacciones.models.modelsWS.modelTransaccion;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by luispineda on 21/09/17.
 */

public class TransacList {

    public static final String ESTADO_ACTIVO_SIN_CIERRE = "A";
    public static final String ESTADO_DEVOLUCION_SIN_CIERRE = "D";

    public static final String PROPERTY_TRANSAC_RESULT = "transacList";

    private static final String KEY_CIERRE_CODIGO_TERMINAL = "codigoTerminal";
    private static final String KEY_CIERRE_NUMERO_APROBACION = "numeroAprobacion";
    private static final String KEY_CIERRE_CEDULA_USUARIO = "cedulaUsuario";
    private static final String KEY_CIERRE_VALOR_APROBADO = "valorAprobado";
    private static final String KEY_CIERRE_ESTADO = "estado";


    private String codigoTerminal;
    private String numeroAprobacion;
    private String cedulaUsuario;
    private String valorAprobado;
    private String estado;

    public TransacList() {
    }

    /**
     * @param codigoTerminal
     * @param numeroAprobacion
     * @param cedulaUsuario
     * @param valorAprobado
     * @param estado
     */
    public TransacList(String codigoTerminal, String numeroAprobacion, String cedulaUsuario, String valorAprobado, String estado) {
        this.codigoTerminal = codigoTerminal;
        this.numeroAprobacion = numeroAprobacion;
        this.cedulaUsuario = cedulaUsuario;
        this.valorAprobado = valorAprobado;
        this.estado = estado;
    }

    public TransacList(SoapObject soap) {

        this.codigoTerminal = soap.getPropertyAsString(KEY_CIERRE_CODIGO_TERMINAL).equals("anyType{}") ? "" : soap.getPropertyAsString(KEY_CIERRE_CODIGO_TERMINAL);
        this.numeroAprobacion = soap.getPropertyAsString(KEY_CIERRE_NUMERO_APROBACION).equals("anyType{}") ? "" : soap.getPropertyAsString(KEY_CIERRE_NUMERO_APROBACION);
        this.cedulaUsuario = soap.getPropertyAsString(KEY_CIERRE_CEDULA_USUARIO).equals("anyType{}") ? "" : soap.getPropertyAsString(KEY_CIERRE_CEDULA_USUARIO);
        this.valorAprobado = soap.getPropertyAsString(KEY_CIERRE_VALOR_APROBADO).equals("anyType{}") ? "" : soap.getPropertyAsString(KEY_CIERRE_VALOR_APROBADO);
        this.estado = soap.getPropertyAsString(KEY_CIERRE_ESTADO).equals("anyType{}") ? "" : soap.getPropertyAsString(KEY_CIERRE_ESTADO);

    }

    public String getCodigoTerminal() {
        return codigoTerminal;
    }

    public void setCodigoTerminal(String codigoTerminal) {
        this.codigoTerminal = codigoTerminal;
    }

    public String getNumeroAprobacion() {
        return numeroAprobacion;
    }

    public void setNumeroAprobacion(String numeroAprobacion) {
        this.numeroAprobacion = numeroAprobacion;
    }

    public String getCedulaUsuario() {
        return cedulaUsuario;
    }

    public void setCedulaUsuario(String cedulaUsuario) {
        this.cedulaUsuario = cedulaUsuario;
    }

    public String getValorAprobado() {
        return valorAprobado;
    }

    public void setValorAprobado(String valorAprobado) {
        this.valorAprobado = valorAprobado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
