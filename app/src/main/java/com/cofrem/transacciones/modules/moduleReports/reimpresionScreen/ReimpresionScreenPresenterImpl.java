package com.cofrem.transacciones.modules.moduleReports.reimpresionScreen;

import android.content.Context;

import com.cofrem.transacciones.lib.EventBus;
import com.cofrem.transacciones.lib.GreenRobotEventBus;
import com.cofrem.transacciones.models.Transaccion;
import com.cofrem.transacciones.modules.moduleReports.reimpresionScreen.events.ReimpresionScreenEvent;
import com.cofrem.transacciones.modules.moduleReports.reimpresionScreen.ui.ReimpresionScreenView;

public class ReimpresionScreenPresenterImpl implements ReimpresionScreenPresenter {

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
    //Instanciamiento de la interface reimpresionScreenView
    private ReimpresionScreenView reimpresionScreenView;

    //Instanciamiento de la interface ReimpresionScreenInteractor
    private ReimpresionScreenInteractor reimpresionScreenInteractor;

    /**
     * #############################################################################################
     * Constructor de la clase
     * #############################################################################################
     *
     * @param reimpresionScreenView
     */
    public ReimpresionScreenPresenterImpl(ReimpresionScreenView reimpresionScreenView) {
        this.reimpresionScreenView = reimpresionScreenView;
        this.reimpresionScreenInteractor = new ReimpresionScreenInteractorImpl();
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
        reimpresionScreenView = null;
        eventBus.unregister(this);
    }

    @Override
    public void validarClaveAdministrador(Context context, String clave) {
        reimpresionScreenInteractor.validarClaveAdministrador(context, clave);
    }

    @Override
    public void validarExistenciaUltimoRecibo(Context context) {
        reimpresionScreenInteractor.validarExistenciaUltimoRecibo(context);
    }

    @Override
    public void imprimirUltimoRecibo(Context context) {
        reimpresionScreenInteractor.imprimirUltimoRecibo(context);
    }

    @Override
    public void validarExistenciaReciboConNumCargo(Context context, String numCargo) {
        reimpresionScreenInteractor.validarExistenciaReciboConNumCargo(context, numCargo);
    }

    @Override
    public void imprimirReciboConNumCargo(Context context) {
        reimpresionScreenInteractor.imprimirReciboConNumCargo(context);
    }

    @Override
    public void validarExistenciaDetalleRecibos(Context context) {
        reimpresionScreenInteractor.validarExistenciaDetalleRecibos(context);
    }

    @Override
    public void imprimirReporteDetalle(Context context) {
        reimpresionScreenInteractor.imprimirReporteDetalle(context);
    }

    @Override
    public void imprimirReporteGeneral(Context context) {
        reimpresionScreenInteractor.imprimirReporteGeneral(context);
    }


    @Override
    public void cierreDeLote(Context context) {
        reimpresionScreenInteractor.cierreDeLote(context);
    }

    @Override
    public void imprimirCierreLote(Context context) {
        reimpresionScreenInteractor.imprimirCierreLote(context);
    }

    /**
     * Metodo para la verificacion de los datos
     */
    @Override
    public void VerifySuccess() {
        if (reimpresionScreenView != null) {
            // reimpresionScreenInteractor.validarPasswordTecnico();
        }
    }


    /**
     * Sobrecarga del metodo onEventMainThread de la interface SaldoScreenPresenter para el manejo de eventos
     *
     * @param reimpresionScreenEvent
     */
    @Override
    public void onEventMainThread(ReimpresionScreenEvent reimpresionScreenEvent) {
        switch (reimpresionScreenEvent.getEventType()) {

            case ReimpresionScreenEvent.onVerifyExistenceReciboPorNumCargoSuccess:
                onVerifyExistenceReciboPorNumCargoSuccess(reimpresionScreenEvent.getModelTransaccion());
                break;
            case ReimpresionScreenEvent.onVerifyExistenceReciboPorNumCargoError:
                onVerifyExistenceReciboPorNumCargoError();
                break;
            case ReimpresionScreenEvent.onVerifyExistenceUltimoReciboSuccess:
                onVerifyExistenceUltimoReciboSuccess(reimpresionScreenEvent.getModelTransaccion());
                break;
            case ReimpresionScreenEvent.onVerifyExistenceUltimoReciboError:
                onVerifyExistenceUltimoReciboError();
                break;
            case ReimpresionScreenEvent.onVerifyClaveAdministradorSuccess:
                onVerifyClaveAdministradorSuccess();
                break;
            case ReimpresionScreenEvent.onVerifyClaveAdministradorError:
                onVerifyClaveAdministradorError();
                break;
            case ReimpresionScreenEvent.onVerifyExistenceReporteDetalleSuccess:
                onVerifyExistenceReporteDetalleSuccess();
                break;
            case ReimpresionScreenEvent.onVerifyExistenceReporteDetalleError:
                onVerifyExistenceReporteDetalleError();
                break;
            case ReimpresionScreenEvent.onImprimirUltimoReciboSuccess:
                onImprimirUltimoReciboSuccess();
                break;
            case ReimpresionScreenEvent.onImprimirUltimoReciboError:
                onImprimirUltimoReciboError(reimpresionScreenEvent.getErrorMessage());
                break;
            case ReimpresionScreenEvent.onImprimirReciboPorNumCargoSuccess:
                onImprimirReciboPorNumCargoSuccess();
                break;
            case ReimpresionScreenEvent.onImprimirReciboPorNumCargoError:
                onImprimirReciboPorNumCargoError(reimpresionScreenEvent.getErrorMessage());
                break;
            case ReimpresionScreenEvent.onImprimirReporteDetalleSuccess:
                onImprimirReporteDetalleSuccess();
                break;
            case ReimpresionScreenEvent.onImprimirReporteDetalleError:
                onImprimirReporteDetalleError(reimpresionScreenEvent.getErrorMessage());
                break;
            case ReimpresionScreenEvent.onImprimirReporteGeneralSuccess:
                onImprimirImprimirReporteGeneralSuccess();
                break;
            case ReimpresionScreenEvent.onImprimirReporteGeneralError:
                onImprimirImprimirReporteGeneralError(reimpresionScreenEvent.getErrorMessage());
                break;
            case ReimpresionScreenEvent.onCierreLoteSuccess:
                onCierreDeLoteSuccess();
                break;
            case ReimpresionScreenEvent.onCierreLoteError:
                onCierreDeLoteError(reimpresionScreenEvent.getErrorMessage());
                break;
            case ReimpresionScreenEvent.onTransaccionWSConexionError:
                onTransaccionWSConexionError(reimpresionScreenEvent.getErrorMessage());
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
        if (reimpresionScreenView != null) {
            //  reimpresionScreenView.handleTransaccionSuccess();
        }
    }

    public void onVerifyExistenceReciboPorNumCargoSuccess(Transaccion modeltgransaccion) {
        reimpresionScreenView.handleVerifyExistenceReciboPorNumCargoSuccess(modeltgransaccion);
    }


    public void onVerifyExistenceReciboPorNumCargoError() {
        reimpresionScreenView.handleVerifyExistenceReciboPorNumCargoError();
    }


    public void onVerifyExistenceUltimoReciboSuccess(Transaccion modeltgransaccion) {
        reimpresionScreenView.handleVerifyExistenceUltimoReciboSuccess(modeltgransaccion);
    }

    public void onVerifyExistenceUltimoReciboError() {
        reimpresionScreenView.handleVerifyExistenceUltimoReciboError();
    }

    public void onVerifyClaveAdministradorSuccess() {
        reimpresionScreenView.handleVerifyClaveAdministradorSuccess();
    }

    public void onVerifyClaveAdministradorError() {
        reimpresionScreenView.handleVerifyClaveAdministradorError();
    }

    private void onVerifyExistenceReporteDetalleSuccess() {
        reimpresionScreenView.handleVerifyExistenceReporteDetalleSuccess();
    }

    private void onVerifyExistenceReporteDetalleError() {
        reimpresionScreenView.handleVerifyExistenceReporteDetalleError();
    }

    private void onImprimirUltimoReciboSuccess() {
        reimpresionScreenView.handleImprimirUltimoReciboSuccess();
    }

    private void onImprimirUltimoReciboError(String error) {
        reimpresionScreenView.handleImprimirUltimoReciboError(error);
    }

    private void onImprimirReciboPorNumCargoSuccess() {
        reimpresionScreenView.handleImprimirReciboPorNumCargoSuccess();
    }

    private void onImprimirReciboPorNumCargoError(String error) {
        reimpresionScreenView.handleImprimirReciboPorNumCargoError(error);
    }

    private void onImprimirReporteDetalleSuccess() {
        reimpresionScreenView.handleImprimirReporteDetalleSuccess();
    }

    private void onImprimirReporteDetalleError(String error) {
        reimpresionScreenView.handleImprimirReporteDetalleError(error);
    }

    private void onImprimirImprimirReporteGeneralSuccess() {
        reimpresionScreenView.handleImprimirReporteGeneralSuccess();
    }

    private void onImprimirImprimirReporteGeneralError(String error) {
        reimpresionScreenView.handleImprimirReporteGeneralError(error);
    }

    private void onCierreDeLoteSuccess() {
        reimpresionScreenView.handleCierreDeLoteSuccess();
    }

    private void onCierreDeLoteError(String error) {
        reimpresionScreenView.handleCierreDeLoteError(error);
    }

    private void onTransaccionWSConexionError(String error) {
        reimpresionScreenView.handleTransaccionWSConexionError(error);
    }
}
