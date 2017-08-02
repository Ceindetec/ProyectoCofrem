package com.cofrem.transacciones.models;

public class Transaccion {

    /**
     * ## TABLA ENTREGADA POR COFREM ########
     * ## Version 2017/07/27 ################
     * Valores posibles que puede tomar el atributo tipo encriptacion
     * 0	SIN ENCRIPTAR
     * 1	ALGORITMO COFREM
     * 2	MD5
     * 3	DOBLE MD5
     * 4	SHA1
     * 5	DOBLE SHA1
     * 6	SHA2
     * 7	DOBLE SHA2
     */
    public final static int CODIGO_ENCR_NO_ENCRIPTADO = 0;
    public final static int CODIGO_ENCR_ALGOR_COFREM = 1;
    public final static int CODIGO_ENCR_MD5 = 2;
    public final static int CODIGO_ENCR_DOBLE_MD5 = 3;
    public final static int CODIGO_ENCR_SHA1 = 4;
    public final static int CODIGO_ENCR_DOBLE_SHA1 = 5;
    public final static int CODIGO_ENCR_SHA2 = 6;
    public final static int CODIGO_ENCR_DOBLE_SHA2 = 7;
    /**
     * ## TABLA ENTREGADA POR COFREM ########
     * ## Version 2017/07/27 ################
     * Valores posibles que puede tomar el atributo tipo_transaccion
     * 1	CONSUMO O COMPRA
     * 2	AVANCE O RETIRO
     */
    public final static int CODIGO_TIPO_TRANSACCION_CONSUMO = 1;
    public final static int CODIGO_TIPO_TRANSACCION_AVANCE = 2;

    /**
     * ## TABLA ENTREGADA POR COFREM ########
     * ## Version 2017/07/27 ################
     * Valores posibles que puede tomar el atributo tipo_producto
     * 01	CUPO ROTATIVO
     * 02	BONO DE BIENESTAR
     * 03	TARJETA REGALO
     * 04	BONO ALIMENTARIO FOSFEC
     * 05	CUOTA MONETARIA FOSFEC
     * 06	CUOTA MONETARIA
     */
    public final static int CODIGO_PRODUCTO_CUPO_ROTATIVO = 1;
    public final static int CODIGO_PRODUCTO_BONO_BIENESTAR = 2;
    public final static int CODIGO_PRODUCTO_TARJETA_REGALO = 3;
    public final static int CODIGO_PRODUCTO_BONO_ALIMENTO_FOSFEC = 4;
    public final static int CODIGO_PRODUCTO_CUOTA_MONTETARIA_FOSFEC = 5;
    public final static int CODIGO_PRODUCTO_CUOTA_MONETARIA = 6;


    /**
     * Modelo para el registro del Establecimiento
     */
    private int id;
    private int numero_cargo;

    private String numero_documento;
    private int clave;
    private String numero_tarjeta;

    //Array de servicios
    private int tipo_producto;
    private int valor;

    private int tipo_encriptacion;
    private int tipo_transaccion;

    private String registro;
    private int estado;

    /**
     * Constructor vacio de la clase
     */
    public Transaccion() {

    }

    public Transaccion(
            int id,
            int numero_cargo,
            String numero_documento,
            int clave, String numero_tarjeta,
            int tipo_producto,
            int valor,
            int tipo_encriptacion,
            int tipo_transaccion,
            String registro,
            int estado) {
        this.id = id;
        this.numero_cargo = numero_cargo;
        this.numero_documento = numero_documento;
        this.clave = clave;
        this.numero_tarjeta = numero_tarjeta;
        this.tipo_producto = tipo_producto;
        this.valor = valor;
        this.tipo_encriptacion = tipo_encriptacion;
        this.tipo_transaccion = tipo_transaccion;
        this.registro = registro;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero_cargo() {
        return numero_cargo;
    }

    public void setNumero_cargo(int numero_cargo) {
        this.numero_cargo = numero_cargo;
    }

    public String getNumero_documento() {
        return numero_documento;
    }

    public void setNumero_documento(String numero_documento) {
        this.numero_documento = numero_documento;
    }

    public int getClave() {
        return clave;
    }

    public void setClave(int clave) {
        this.clave = clave;
    }

    public String getNumero_tarjeta() {
        return numero_tarjeta;
    }

    public void setNumero_tarjeta(String numero_tarjeta) {
        this.numero_tarjeta = numero_tarjeta;
    }

    public int getTipo_producto() {
        return tipo_producto;
    }

    public void setTipo_producto(int tipo_producto) {
        this.tipo_producto = tipo_producto;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public int getTipo_encriptacion() {
        return tipo_encriptacion;
    }

    public void setTipo_encriptacion(int tipo_encriptacion) {
        this.tipo_encriptacion = tipo_encriptacion;
    }

    public int getTipo_transaccion() {
        return tipo_transaccion;
    }

    public void setTipo_transaccion(int tipo_transaccion) {
        this.tipo_transaccion = tipo_transaccion;
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
