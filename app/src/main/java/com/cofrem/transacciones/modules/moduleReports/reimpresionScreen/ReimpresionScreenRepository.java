package com.cofrem.transacciones.modules.moduleReports.reimpresionScreen;

import android.content.Context;

public interface ReimpresionScreenRepository {

    void validateAcces();


    /**
     * Metodo que validara la existencua de un ultimo recibo
     * - Si existe un ultimo recibo enviara un modelo de la transaccion
     * - si NO exite un recibo solo notifica que no existe
     *
     * @param context , numCargo
     */
    void validarExistenciaUltimoRecibo(Context context);

    /**
     * Metodo que se encargara de imprimir el ultimo recibo
     *
     * @param context
     */
    void imprimirUltimoRecibo(Context context);

    /**
     * Metodo que validara la existencua de recibo especificando el numero de cargo
     * - Si existe un recibo con el numero de cargo enviara un modelo de la transaccion
     * - si NO exite un recibo solo notifica que no existe
     *
     * @param context , numCargo
     */
    void validarExistenciaReciboConNumCargo(Context context, String numCargo);

    /**
     * Metodo que se encargara de imprimir el recibo por número de cargo
     *
     * @param context
     */
    void imprimirReciboConNumCargo(Context context);

    /**
     * Metodo que validara la clave del administrador para dar paso a la reimpresion de recibos:
     *
     * @param context , numCargo
     */
    void validarClaveAdministrador(Context context, String clave);

    /**
     * Metodo que validara la existencua de recibos
     * - Si existen recibos enviara una lista de modelo de la transaccion
     * - si NO exite un recibo solo notifica que no existen
     *
     * @param context , numCargo
     */
    void validarExistenciaDetalleRecibos(Context context);

    /**
     * Metodo que se encargara de imprimir el reporte Detalle
     *
     * @param context
     */
    void imprimirReporteDetalle(Context context);

    /**
     * Metodo que se encargara de imprimir el reporte General
     *
     * @param context
     */
    void imprimirReporteGeneral(Context context);


    void cierreDeLote(Context context);


    void imprimirCierreLote(Context context);


}
