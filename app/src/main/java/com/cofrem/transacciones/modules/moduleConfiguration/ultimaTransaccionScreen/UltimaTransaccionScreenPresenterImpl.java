package com.cofrem.transacciones.modules.moduleConfiguration.ultimaTransaccionScreen;

import android.content.Context;

import com.cofrem.transacciones.lib.EventBus;
import com.cofrem.transacciones.lib.GreenRobotEventBus;
import com.cofrem.transacciones.modules.moduleConfiguration.ultimaTransaccionScreen.events.UltimaTransaccionScreenEvent;
import com.cofrem.transacciones.modules.moduleConfiguration.ultimaTransaccionScreen.ui.UltimaTransaccionScreenView;

public class UltimaTransaccionScreenPresenterImpl implements UltimaTransaccionScreenPresenter {


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
    //Instanciamiento de la interface ultimaTransaccionScreenView
    private UltimaTransaccionScreenView ultimaTransaccionScreenView;

    //Instanciamiento de la interface UltimaTransaccionScreenInteractor
    private UltimaTransaccionScreenInteractor ultimaTransaccionScreenInteractor;

    /**
     * #############################################################################################
     * Constructor de la clase
     * #############################################################################################
     *
     * @param ultimaTransaccionScreenView
     */
    public UltimaTransaccionScreenPresenterImpl(UltimaTransaccionScreenView ultimaTransaccionScreenView) {
        this.ultimaTransaccionScreenView = ultimaTransaccionScreenView;
        this.ultimaTransaccionScreenInteractor = new UltimaTransaccionScreenInteractorImpl();
        this.eventBus = GreenRobotEventBus.getInstance();
    }

    /**
     * Sobrecarga del metodo onCreate de la interface SaldoScreenPresenter "crear" el registro al bus de eventos
     */
    @Override
    public void onCreate() {

        eventBus.register(this);

    }

    /**
     * Sobrecarga del metodo onDestroy de la interface SaldoScreenPresenter para "eliminar"  el registro al bus de eventos
     */
    @Override
    public void onDestroy() {
        ultimaTransaccionScreenView = null;
        eventBus.unregister(this);
    }

    /**
     * @param context
     */
    @Override
    public void validarUltimaTransaccion(Context context) {
        ultimaTransaccionScreenInteractor.validarUltimaTransaccion(context);
    }

    /**
     * Sobrecarga del metodo onEventMainThread de la interface SaldoScreenPresenter para el manejo de eventos
     *
     * @param ultimaTransaccionScreenEvent
     */
    @Override
    public void onEventMainThread(UltimaTransaccionScreenEvent ultimaTransaccionScreenEvent) {
        switch (ultimaTransaccionScreenEvent.getEventType()) {

            case UltimaTransaccionScreenEvent.onValidarUltimaTransaccionCorrecta:
                onValidarUltimaTransaccionCorrecta();
                break;

            case UltimaTransaccionScreenEvent.onValidarUltimaTransaccionErronea:
                onValidarUltimaTransaccionErronea();
                break;

            case UltimaTransaccionScreenEvent.onValidarUltimaTransaccionError:
                onValidarUltimaTransaccionError(ultimaTransaccionScreenEvent.getErrorMessage());
                break;

            case UltimaTransaccionScreenEvent.onTransaccionWSConexionError:
                onTransaccionWSConexionError();
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
    private void onValidarUltimaTransaccionCorrecta() {
        if (ultimaTransaccionScreenView != null) {
            ultimaTransaccionScreenView.onValidarUltimaTransaccionCorrecta();
        }
    }

    /**
     * Metodo para manejar la verificacion exitosa
     */
    private void onValidarUltimaTransaccionErronea() {
        if (ultimaTransaccionScreenView != null) {
            ultimaTransaccionScreenView.onValidarUltimaTransaccionErronea();
        }
    }

    /**
     * Metodo para manejar la verificacion exitosa
     */
    private void onValidarUltimaTransaccionError(String error) {
        if (ultimaTransaccionScreenView != null) {
            ultimaTransaccionScreenView.onValidarUltimaTransaccionError(error);
        }
    }

    /**
     * Metodo para manejar la verificacion exitosa
     */
    private void onTransaccionWSConexionError() {
        if (ultimaTransaccionScreenView != null) {
            ultimaTransaccionScreenView.handleTransaccionWSConexionError();
        }
    }

}
