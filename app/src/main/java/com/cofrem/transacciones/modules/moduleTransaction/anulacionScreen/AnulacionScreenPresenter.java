package com.cofrem.transacciones.modules.moduleTransaction.anulacionScreen;

import android.content.Context;

import com.cofrem.transacciones.models.Transaccion;
import com.cofrem.transacciones.modules.moduleTransaction.anulacionScreen.events.AnulacionScreenEvent;

public interface AnulacionScreenPresenter {

    /**
     * @param context
     * @param passAdmin
     */
    void validarPasswordAdministrador(Context context, String passAdmin);

    /**
     * @param context
     * @param numeroCargo
     */
    void obtenerValorTransaccion(Context context, String numeroCargo);

    /**
     * @param context
     * @param transaccion
     */
    void registrarTransaccion(Context context, Transaccion transaccion);

    /**
     * Metodo para imprimir la copia para el cliente
     */
    void imprimirRecibo(Context context, String stringCopia);

    /**
     * Metodo para la creacion del presentador
     */
    void onCreate();

    /**
     * Metodo para la destruccion del presentador
     */
    void onDestroy();


    /**
     * Metodo para recibir los eventos generados
     *
     * @param anulacionScreenEvent
     */
    void onEventMainThread(AnulacionScreenEvent anulacionScreenEvent);

}
