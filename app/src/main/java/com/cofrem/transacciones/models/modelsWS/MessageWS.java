package com.cofrem.transacciones.models.modelsWS;

import org.ksoap2.serialization.SoapObject;

public class MessageWS {


    //Modelo usado en la respuesta del WS para la respuestas

    //Modelado de Properties y Keys
    public static final String PROPERTY_MESSAGE = "mensaje";
    public static final String PROPERTY_TRANSAC_LISTS = "transacListResult";
    public static final String KEY_CODIGO_MESSAGE = "codigoMensaje";
    public static final String KEY_DETALLE_MESSAGE = "detalleMensaje";

    /**
     * STATUS METHOD TERMINAL
     */
    /**
     * ## TABLA ENTREGADA POR COFREM ########
     * ## Version 2017/07/27 ################
     * Valores posibles que puede tomar el atributo tipo encriptacion
     * 00	TRANSACCIÓN EXITOSA
     * 01	TARJETAHABIENTE NO EXISTE
     * 02	CLAVE ERRARA
     * 03	TARJETAHABIENTE INACTIVO
     * 04	TARJETAHABIENTE EN MORA
     * 05	TARJETAHABIENTE SIN CUPO DISPONIBLE
     * 06	EMPRESA NO EXISTE
     * 07	EMPRESA INACTIVA
     * 08	EMPRESA EN MORA
     * 09	TRANSACCIÓN NO ENCONTRADA
     * 10	CEDULA Y TARJETA NO EXISTE
     * 11	CONSULTA EXITOSA
     * 12	TERMINAL NO EXISTE
     * 13	TERMINAL YA EXISTE
     * 14	PUNTO NO EXISTE
     * 15	…
     * 16	LA TERMINAL YA TIENE UN UUID ASIGNADO
     * 17	UUID YA EXISTE EN OTRA TERMINAL
     * 18	UUID NO VALIDO
     * 19	LA TERMINAL YA TIENE UNA MAC ASIGNADA
     * 20	LA MAC YA EXISTE EN OTRA TERMINAL
     * 21	MAC NO VALIDO
     * 22	NO SE PUDO ACTUALIZAR LA INFORMACIÓN
     * 30	TARJETA NO PERMITIDA EN ESTA TERMINAL
     * 98	NO HAY CONEXIÓN CON LA BASE DE DATOS
     * 99	ERROR INTERSECTADO: ERROR EXCEPCIÓN
     */
    public final static int statusTransaccionExitosa = 0;
    public final static int statusTarjetaHabienteNoExiste = 1;
    public final static int statusClaveErrada = 2;
    public final static int statusTarjetHabienteInactivo = 3;
    public final static int statusTarjetaHabienteMora = 4;
    public final static int statusTarjetaHabienteSinCupoDisponible = 5;
    public final static int statusEmpresaNoExiste = 6;
    public final static int statusEmpresaInactiva = 7;
    public final static int statusEmpresaMora = 8;
    public final static int statusTransaccionNoEncontrada = 9;
    public final static int statusCedulaTarjetaNoExiste = 10;
    public final static int statusConsultaExitosa = 11;
    public final static int statusTerminalNoExiste = 12;
    public final static int statusTerminalExiste = 13;
    public final static int statusPuntoNoExiste = 14;
    public final static int statusEmpty = 15;
    public final static int statusTerminalUUIDYaAsignado = 16;
    public final static int statusTerminalUUIDExistente = 17;
    public final static int statusTerminalUUIDNoValido = 18;
    public final static int statusTerminalMACYaAsignada = 19;
    public final static int statusTerminalMACExistente = 20;
    public final static int statusTerminalMACNoValida = 21;
    public final static int statusActualizacionNoRealizada = 22;
    public final static int statusTarjetaNoPermitidaEnTerminal = 30;
    public final static int statusTransaccionYaAnulada = 30;
    public final static int statusTransaccionNoFechaActual = 32;
    public final static int statusTransaccionEstasdoDiferente = 36;
    public final static int statusErrorDatabase = 98;
    public final static int statusTerminalErrorException = 99;

    //Informacion del detalleMensaje
    private int codigoMensaje;
    private String detalleMensaje;

    /**
     * Constructor de la clase que recibe un Objeto tipo SoapObject
     *
     * @param soap
     */
    public MessageWS(SoapObject soap) {

        this.codigoMensaje = soap.getPropertyAsString(KEY_CODIGO_MESSAGE).equals("anyType{}") ? statusTransaccionExitosa : Integer.parseInt(soap.getPropertyAsString(KEY_CODIGO_MESSAGE));

        this.detalleMensaje = soap.getPropertyAsString(KEY_DETALLE_MESSAGE).equals("anyType{}") ? "" : soap.getPropertyAsString(KEY_DETALLE_MESSAGE);

    }

    public int getCodigoMensaje() {
        return codigoMensaje;
    }

    public void setCodigoMensaje(int codigoMensaje) {
        this.codigoMensaje = codigoMensaje;
    }

    public String getDetalleMensaje() {
        return detalleMensaje;
    }

    public void setDetalleMensaje(String detalleMensaje) {
        this.detalleMensaje = detalleMensaje;
    }
}