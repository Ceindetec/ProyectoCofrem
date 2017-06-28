package com.cofrem.transacciones.Modules.ModuleConfiguration.TestCommunicationScreen;

import com.cofrem.transacciones.Modules.ModuleConfiguration.TestCommunicationScreen.events.TestCommunicationScreenEvent;
import com.cofrem.transacciones.Modules.ModuleConfiguration.TestCommunicationScreen.ui.GenericScreenView;
import com.cofrem.transacciones.lib.EventBus;
import com.cofrem.transacciones.lib.GreenRobotEventBus;

public class TestCommunicationScreenPresenterImpl implements TestCommunicationScreenPresenter {


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
    //Instanciamiento de la interface genericScreenView
    private GenericScreenView genericScreenView;

    //Instanciamiento de la interface TestCommunicationScreenInteractor
    private TestCommunicationScreenInteractor testCommunicationScreenInteractor;

    /**
     * #############################################################################################
     * Constructor de la clase
     * #############################################################################################
     *
     * @param genericScreenView
     */
    public TestCommunicationScreenPresenterImpl(GenericScreenView genericScreenView) {
        this.genericScreenView = genericScreenView;
        this.testCommunicationScreenInteractor = new TestCommunicationScreenInteractorImpl();
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
        genericScreenView = null;
        eventBus.unregister(this);
    }

    /**
     * Metodo para la verificacion de los datos
     */
    @Override
    public void VerifySuccess() {
        if (genericScreenView != null) {
            testCommunicationScreenInteractor.validateAccess();
        }
    }

    /**
     * Sobrecarga del metodo onEventMainThread de la interface SaldoScreenPresenter para el manejo de eventos
     *
     * @param testCommunicationScreenEvent
     */
    @Override
    public void onEventMainThread(TestCommunicationScreenEvent testCommunicationScreenEvent) {
        switch (testCommunicationScreenEvent.getEventType()) {

            case TestCommunicationScreenEvent.onVerifySuccess:
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
        if (genericScreenView != null) {
            genericScreenView.handleVerifySuccess();
        }
    }

}
