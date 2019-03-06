package com.cofrem.transacciones.modules.moduleConfiguration.testCommunicationScreen;

import android.content.Context;

import com.cofrem.transacciones.modules.moduleConfiguration.testCommunicationScreen.events.TestCommunicationScreenEvent;

public interface TestCommunicationScreenPresenter {

    //Todo: crear metodos presentador

    void testComunication(Context context);


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
     * Metodo para recibir los eventos generados
     *
     * @param splashScreenEvent
     */
    void onEventMainThread(TestCommunicationScreenEvent splashScreenEvent);

}
