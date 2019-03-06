package com.cofrem.transacciones.modules.moduleConfiguration.ultimaTransaccionScreen;

import android.content.Context;

import com.cofrem.transacciones.modules.moduleConfiguration.ultimaTransaccionScreen.events.UltimaTransaccionScreenEvent;

public interface UltimaTransaccionScreenPresenter {

    /**
     * @param context
     */
    void validarUltimaTransaccion(Context context);

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
     * @param splashScreenEvent
     */
    void onEventMainThread(UltimaTransaccionScreenEvent splashScreenEvent);

}
