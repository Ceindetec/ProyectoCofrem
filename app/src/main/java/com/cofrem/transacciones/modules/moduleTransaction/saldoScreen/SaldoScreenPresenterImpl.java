package com.cofrem.transacciones.modules.moduleTransaction.saldoScreen;

import android.content.Context;

import com.cofrem.transacciones.lib.EventBus;
import com.cofrem.transacciones.lib.GreenRobotEventBus;
import com.cofrem.transacciones.models.Transaccion;
import com.cofrem.transacciones.models.modelsWS.modelTransaccion.InformacionSaldo;
import com.cofrem.transacciones.modules.moduleTransaction.saldoScreen.events.SaldoScreenEvent;
import com.cofrem.transacciones.modules.moduleTransaction.saldoScreen.ui.SaldoScreenView;

public class SaldoScreenPresenterImpl implements SaldoScreenPresenter {


    /**
     * #############################################################################################
     * Declaracion de componentes y variables
     * #############################################################################################
     */
    //Declaracion del bus de eventos
    EventBus eventBus;

    /**
     * #############################################################################################
     * Instanciamientos de las clases
     * #############################################################################################
     */
    //Instanciamiento de la interface saldoScreenView
    private SaldoScreenView saldoScreenView;

    //Instanciamiento de la interface SaldoScreenInteractor
    private SaldoScreenInteractor saldoScreenInteractor;

    /**
     * #############################################################################################
     * Constructor de la clase
     * #############################################################################################
     *
     * @param saldoScreenView
     */
    public SaldoScreenPresenterImpl(SaldoScreenView saldoScreenView) {
        this.saldoScreenView = saldoScreenView;
        this.saldoScreenInteractor = new SaldoScreenInteractorImpl();
        this.eventBus = GreenRobotEventBus.getInstance();
    }

    /**
     * Sobrecarga del metodo onCreate de la interface AnulacionScreenPresenter "crear" el registro al bus de eventos
     */
    @Override
    public void onCreate() {

        eventBus.register(this);

    }

    /**
     * Sobrecarga del metodo onDestroy de la interface AnulacionScreenPresenter para "eliminar"  el registro al bus de eventos
     */
    @Override
    public void onDestroy() {
        saldoScreenView = null;
        eventBus.unregister(this);
    }

    /**
     * Metodo para la verificacion de los datos
     */
    @Override
    public void registrarTransaccion(Context context, Transaccion transaccion) {
        if (saldoScreenView != null) {
            saldoScreenInteractor.registrarTransaccion(context, transaccion);
        }
    }

    @Override
    public void imprimirRecibo(Context context) {
        saldoScreenInteractor.imprimirRecibo(context);
    }

    /**
     * Sobrecarga del metodo onEventMainThread de la interface AnulacionScreenPresenter para el manejo de eventos
     *
     * @param saldoScreenEvent
     */
    @Override
    public void onEventMainThread(SaldoScreenEvent saldoScreenEvent) {
        switch (saldoScreenEvent.getEventType()) {

            case SaldoScreenEvent.onTransaccionSuccess:
                onTransaccionSuccess(saldoScreenEvent.getInformacionSaldo());
                break;

            case SaldoScreenEvent.onTransaccionWSRegisterError:
                onTransaccionWSRegisterError(saldoScreenEvent.getErrorMessage());
                break;

            case SaldoScreenEvent.onTransaccionWSConexionError:
                onTransaccionWSConexionError();
                break;

            case SaldoScreenEvent.onImprecionReciboSuccess:
                onImprimirSuccess();
                break;

            case SaldoScreenEvent.onImprecionReciboError:
                onImprimirError(saldoScreenEvent.getErrorMessage());
                break;

        }
    }


    /**
     * #############################################################################################
     * Metodo propios de la clase
     * #############################################################################################
     */

    /**
     * Metodo para manejar la verificacion exitosa
     */
    private void onTransaccionSuccess(InformacionSaldo informacionSaldo) {
        if (saldoScreenView != null) {
            saldoScreenView.handleTransaccionSuccess(informacionSaldo);
        }
    }

    private void onTransaccionWSRegisterError(String errorMessage) {
        if (saldoScreenView != null) {
            saldoScreenView.handleTransaccionWSRegisterError(errorMessage);
        }
    }

    private void onTransaccionWSConexionError() {
        if (saldoScreenView != null) {
            saldoScreenView.handleTransaccionWSConexionError();
        }
    }

    /**
     * Metodo para manejar la transaccion del Web Service Correcta
     */
    private void onImprimirSuccess() {
        if (saldoScreenView != null) {
            saldoScreenView.handleImprimirReciboSuccess();
        }
    }

    /**
     * Metodo para manejar la transaccion del Web Service Correcta
     */
    private void onImprimirError(String errorMessage) {
        if (saldoScreenView != null) {
            saldoScreenView.handleImprimirReciboError(errorMessage);
        }
    }
}
