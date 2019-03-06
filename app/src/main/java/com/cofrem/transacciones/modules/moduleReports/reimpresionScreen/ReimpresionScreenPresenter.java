package com.cofrem.transacciones.modules.moduleReports.reimpresionScreen;

import android.content.Context;

import com.cofrem.transacciones.modules.moduleReports.reimpresionScreen.events.ReimpresionScreenEvent;

public interface ReimpresionScreenPresenter {

    //Todo: crear metodos presentador

    /**
     * metodo presentador
     */
    void VerifySuccess();

    /**
     * Metodo para la creacion del presentador
     */
    void onCreate();

    /**
     * Metodo para la destruccion del presentador
     */
    void onDestroy();

    /**
     * Metodo que validara la existencia de un ultimo recibo el cual se pueda imprimir:
     *
     * @param context
     */
    void validarExistenciaUltimoRecibo(Context context);

    /**
     * Metodo que se encargara de imprimir el ultimo recibo
     *
     * @param context
     */
    void imprimirUltimoRecibo(Context context);

    /**
     * Metodo que validara la existencia de un recibo por numero de cargo:
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
     * Metodo que validara la calve del administrador para dar paso a la reimpresion de recibos:
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

    /**
     * Metodo para recibir los eventos generados
     *
     * @param splashScreenEvent
     */
    void onEventMainThread(ReimpresionScreenEvent splashScreenEvent);
}
