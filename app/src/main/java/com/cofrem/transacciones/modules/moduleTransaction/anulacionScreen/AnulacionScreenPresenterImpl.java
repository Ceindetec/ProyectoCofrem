package com.cofrem.transacciones.modules.moduleTransaction.anulacionScreen;

import android.content.Context;

import com.cofrem.transacciones.lib.EventBus;
import com.cofrem.transacciones.lib.GreenRobotEventBus;
import com.cofrem.transacciones.models.Transaccion;
import com.cofrem.transacciones.modules.moduleTransaction.anulacionScreen.events.AnulacionScreenEvent;
import com.cofrem.transacciones.modules.moduleTransaction.anulacionScreen.ui.AnulacionScreenView;

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
     * @param context
     * @param passAdmin
     */
    @Override
    public void validarPasswordAdministrador(Context context, String passAdmin) {
        anulacionScreenInteractor.validarPasswordAdministrador(context, passAdmin);
    }

    /**
     * @param context
     * @param numeroCargo
     */
    @Override
    public void obtenerValorTransaccion(Context context, String numeroCargo) {
        anulacionScreenInteractor.obtenerValorTransaccion(context, numeroCargo);
    }

    /**
     * @param context
     * @param transaccion
     */
    @Override
    public void registrarTransaccion(Context context, Transaccion transaccion) {
        anulacionScreenInteractor.registrarTransaccion(context, transaccion);
    }

    @Override
    public void imprimirRecibo(Context context, String stringCopia) {
        anulacionScreenInteractor.imprimirRecibo(context, stringCopia);
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
     * Sobrecarga del metodo onEventMainThread de la interface AnulacionScreenPresenter para el manejo de eventos
     *
     * @param anulacionScreenEvent
     */
    @Override
    public void onEventMainThread(AnulacionScreenEvent anulacionScreenEvent) {
        switch (anulacionScreenEvent.getEventType()) {

            case AnulacionScreenEvent.onClaveAdministracionValida:
                onClaveAdministracionValida();
                break;

            case AnulacionScreenEvent.onClaveAdministracionNoValida:
                onClaveAdministracionNoValida();
                break;

            case AnulacionScreenEvent.onClaveAdministracionError:
                onClaveAdministracionError();
                break;

            case AnulacionScreenEvent.onNumeroCargoNoRelacionado:
                onNumeroCargoNoRelacionado();
                break;

            case AnulacionScreenEvent.onNumeroCargoRelacionado:
                onNumeroCargoRelacionado(anulacionScreenEvent.getValorInt());
                break;

            case AnulacionScreenEvent.onTransaccionSuccess:
                onTransaccionSuccess();
                break;

            case AnulacionScreenEvent.onTransaccionWSConexionError:
                onTransaccionWSConexionError();
                break;

            case AnulacionScreenEvent.onTransaccionWSRegisterError:
                onTransaccionWSRegisterError(anulacionScreenEvent.getErrorMessage());
                break;

            case AnulacionScreenEvent.onTransaccionDBRegisterError:
                onTransaccionDBRegisterError();
                break;

            case AnulacionScreenEvent.onImprecionReciboSuccess:

                break;
            case AnulacionScreenEvent.onImprecionReciboError:

                break;

        }
    }


    /**
     * #############################################################################################
     * Metodo propios de la clase
     * #############################################################################################
     */

    /**
     * Metodo para manejar la verificacion exitosa de la clave de administracion
     */
    private void onClaveAdministracionValida() {
        if (anulacionScreenView != null) {
            anulacionScreenView.handleClaveAdministracionValida();
        }
    }

    /**
     * Metodo para manejar la verificacion erronea de la clave de administracion
     */
    private void onClaveAdministracionNoValida() {
        if (anulacionScreenView != null) {
            anulacionScreenView.handleClaveAdministracionNoValida();
        }
    }

    /**
     * Metodo para manejar error en la verificacion de la clave de administracion
     */
    private void onClaveAdministracionError() {
        if (anulacionScreenView != null) {
            anulacionScreenView.handleClaveAdministracionError();
        }
    }

    /**
     * Metodo para manejar la el valor no valido en la transaccion
     */
    private void onNumeroCargoNoRelacionado() {
        if (anulacionScreenView != null) {
            anulacionScreenView.handleNumeroCargoNoRelacionado();
        }
    }

    /**
     * Metodo para manejar la el valor valido en la transaccion
     */
    private void onNumeroCargoRelacionado(int valorTransaccion) {
        if (anulacionScreenView != null) {
            anulacionScreenView.handleNumeroCargoRelacionado(valorTransaccion);
        }
    }

    /**
     * Metodo para manejar la transaccion del Web Service Correcta
     */
    private void onTransaccionSuccess() {
        if (anulacionScreenView != null) {
            anulacionScreenView.handleTransaccionSuccess();
        }
    }

    /**
     * Metodo para manejar la conexion del Web Service Erronea
     */
    private void onTransaccionWSConexionError() {
        if (anulacionScreenView != null) {
            anulacionScreenView.handleTransaccionWSConexionError();
        }
    }

    /**
     * Metodo para manejar la transaccion erronea desde el Web Service
     *
     * @param errorMessage
     */
    private void onTransaccionWSRegisterError(String errorMessage) {
        if (anulacionScreenView != null) {
            anulacionScreenView.handleTransaccionWSRegisterError(errorMessage);
        }
    }

    /**
     * Metodo para manejar la transaccion erronea desde la base de datos
     */
    private void onTransaccionDBRegisterError() {
        if (anulacionScreenView != null) {
            anulacionScreenView.handleTransaccionDBRegisterError();
        }
    }
}
