package com.cofrem.transacciones.Modules.ModuleTransaction.AnulacionScreen;

import com.cofrem.transacciones.Modules.ModuleTransaction.AnulacionScreen.events.AnulacionScreenEvent;
import com.cofrem.transacciones.Modules.ModuleTransaction.AnulacionScreen.ui.AnulacionScreenView;
import com.cofrem.transacciones.lib.EventBus;
import com.cofrem.transacciones.lib.GreenRobotEventBus;

public class AnulacionScreenPresenterImpl implements AnulacionScreenPresenter {


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
    //Instanciamiento de la interface anulacionScreenView
    private AnulacionScreenView anulacionScreenView;

    //Instanciamiento de la interface AnulacionScreenInteractor
    private AnulacionScreenInteractor anulacionScreenInteractor;

    /**
     * #############################################################################################
     * Constructor de la clase
     * #############################################################################################
     *
     * @param anulacionScreenView
     */
    public AnulacionScreenPresenterImpl(AnulacionScreenView anulacionScreenView) {
        this.anulacionScreenView = anulacionScreenView;
        this.anulacionScreenInteractor = new AnulacionScreenInteractorImpl();
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
        anulacionScreenView = null;
        eventBus.unregister(this);
    }

    /**
     * Metodo para la verificacion de los datos
     */
    @Override
    public void VerifySuccess() {
        if (anulacionScreenView != null) {
            anulacionScreenInteractor.validateAccess();
        }
    }

    /**
     * Sobrecarga del metodo onEventMainThread de la interface AnulacionScreenPresenter para el manejo de eventos
     *
     * @param anulacionScreenEvent
     */
    @Override
    public void onEventMainThread(AnulacionScreenEvent anulacionScreenEvent) {
        switch (anulacionScreenEvent.getEventType()) {

            case AnulacionScreenEvent.onVerifySuccess:
                onVerifySuccess();
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
    private void onVerifySuccess() {
        if (anulacionScreenView != null) {
            anulacionScreenView.handleVerifySuccess();
        }
    }

}
