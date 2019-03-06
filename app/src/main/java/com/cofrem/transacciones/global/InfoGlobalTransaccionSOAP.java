package com.cofrem.transacciones.global;

public class InfoGlobalTransaccionSOAP {

    /**
     * INFORMACIÓN REGISTRADA PARA EL USO DE WEB SERVICES
     */
    public final static String HTTP = "http://";
    public final static String WEB_SERVICE_URI = "/WsDatafono/Datafono?wsdl";
    public final static String NAME_SPACE = "datafono/";


    /**
     * PARAMETROS Y NOMBRES DE METODO DE LOS WEB SERVICES
     */

    /**
     * Datos para el Web Service Punto
     */
    public final static String METHOD_NAME_PUNTO = "Punto";
    public final static String PARAM_NAME_CODIGO_PUNTO = "codigoPunto";

    /**
     * Datos para el Web Service Terminal
     */
    public final static String METHOD_NAME_TERMINAL = "Terminal";
    public final static String PARAM_NAME_TERMINAL_CODIGO_TERMINAL = "codigoTerminal";

    /**
     * Datos para el Web Service Test
     */
    public final static String METHOD_NAME_TEST = "Test";

    /**
     * Datos para el Web Service Test
     */
    public final static String METHOD_NAME_CIERRE_LOTE = "Cierre";
    public final static String METHOD_NAME_CIERRE_LOTE_INDIVIDUAL = "CierreIndividual";

    public final static String PARAM_NAME_CIERRE_CODIGO_TERMINAL = "codigoTerminal";
    public final static String PARAM_NAME_CIERRE_NUMERO_APROBACION = "numeroAprobacion";
    public final static String PARAM_NAME_CIERRE_CEDULA_USUARIO = "cedulaUsuario";
    public final static String PARAM_NAME_CIERRE_VALOR_APROBADO = "valorAprobado";
    public final static String PARAM_NAME_CIERRE_ESTADO = "estado";

    /**
     * Datos para el Web Service Transaccion
     */
    public final static String METHOD_NAME_TRANSACCION = "Transaccion";
    public final static String METHOD_NAME_VALIDACION = "UltimaTransaccion";

    public final static String PARAM_NAME_TRANSACCION_CODIGO_TERMINAL = "codigoTerminal";
    public final static String PARAM_NAME_TRANSACCION_TIPO_TRANSACCION = "tipoTransaccion";
    public final static String PARAM_NAME_TRANSACCION_CEDULA_USUARIO = "cedulaUsuario";
    public final static String PARAM_NAME_TRANSACCION_NUMERO_TARJETA = "numeroTarjeta";
    public final static String PARAM_NAME_TRANSACCION_CLAVE_USUARIO = "clave";
    public final static String PARAM_NAME_TRANSACCION_TIPO_ENCRIPTACION = "tipoEncrip";
    public final static String PARAM_NAME_TRANSACCION_TIPO_SERVICIO = "tipoServicio";
    public final static String PARAM_NAME_TRANSACCION_VALOR_SOLICITADO = "valorSolicitado";

    /**
     * Datos para el Web Service Anulacion
     */
    public final static String METHOD_NAME_ANULACION = "Anulacion";

    public final static String PARAM_NAME_ANULACION_CODIGO_TERMINAL = "codigoTerminal";
    public final static String PARAM_NAME_ANULACION_NUMERO_APROBACION = "numeroAprobacion";
    public final static String PARAM_NAME_ANULACION_CEDULA_USUARIO = "cedulaUsuario";
    public final static String PARAM_NAME_ANULACION_NUMERO_TARJETA = "numeroTarjeta";
    public final static String PARAM_NAME_ANULACION_CLAVE_USUARIO = "clave";
    public final static String PARAM_NAME_ANULACION_TIPO_ENCRIPTACION = "tipoEncrip";
    public final static String PARAM_NAME_ANULACION_VALOR_APROBADO = "valorAprobado";
    /**
     * Datos para el Web Service Anulacion
     */
    public final static String METHOD_NAME_SALDO = "Saldo";

    public final static String PARAM_NAME_SALDO_CODIGO_TERMINAL = "codigoTerminal";
    public final static String PARAM_NAME_SALDO_CEDULA_USUARIO = "cedulaUsuario";
    public final static String PARAM_NAME_SALDO_NUMERO_TARJETA = "numeroTarjeta";
    public final static String PARAM_NAME_SALDO_CLAVE_USUARIO = "clave";
    public final static String PARAM_NAME_SALDO_TIPO_ENCRIPTACION = "tipoEncrip";


}