package com.cofrem.transacciones.modules.moduleTransaction.saldoScreen;

import android.content.Context;

import com.cofrem.transacciones.models.Transaccion;
import com.cofrem.transacciones.modules.moduleTransaction.saldoScreen.events.SaldoScreenEvent;

public interface SaldoScreenPresenter {

    //Todo: crear metodos presentador

    /**
     * metodo presentador
     */
    void registrarTransaccion(Context context, Transaccion transaccion);

    /**
     * Metodo que imprime el recibo de la consulta de saldo
     *
     * @param context
     */
    void imprimirRecibo(Context context);

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
     * @param saldoScreenEvent
     */
    void onEventMainThread(SaldoScreenEvent saldoScreenEvent);

}
