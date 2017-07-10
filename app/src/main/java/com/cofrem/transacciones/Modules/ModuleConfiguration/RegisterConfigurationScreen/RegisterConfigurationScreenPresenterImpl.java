package com.cofrem.transacciones.Modules.ModuleConfiguration.RegisterConfigurationScreen;

import android.content.Context;

import com.cofrem.transacciones.Modules.ModuleConfiguration.RegisterConfigurationScreen.events.RegisterConfigurationScreenEvent;
import com.cofrem.transacciones.Modules.ModuleConfiguration.RegisterConfigurationScreen.ui.RegisterConfigurationScreenView;
import com.cofrem.transacciones.lib.EventBus;
import com.cofrem.transacciones.lib.GreenRobotEventBus;
import com.cofrem.transacciones.models.Configurations;

public class RegisterConfigurationScreenPresenterImpl implements RegisterConfigurationScreenPresenter {


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
    //Instanciamiento de la interface registerConfigurationScreenView
    private RegisterConfigurationScreenView registerConfigurationScreenView;

    //Instanciamiento de la interface RegisterConfigurationScreenInteractor
    private RegisterConfigurationScreenInteractor registerConfigurationScreenInteractor;

    /**
     * #############################################################################################
     * Constructor de la clase
     * #############################################################################################
     *
     * @param registerConfigurationScreenView
     */
    public RegisterConfigurationScreenPresenterImpl(RegisterConfigurationScreenView registerConfigurationScreenView) {
        this.registerConfigurationScreenView = registerConfigurationScreenView;
        this.registerConfigurationScreenInteractor = new RegisterConfigurationScreenInteractorImpl();
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
        registerConfigurationScreenView = null;
        eventBus.unregister(this);
    }

    /**
     * Valida el acceso a la configuracion del dispositivo mediante la contraseña de administrador
     *
     * @param context
     * @param passAdmin
     */
    @Override
    public void validarPasswordTecnico(Context context, String passAdmin) {
        if (registerConfigurationScreenView != null) {
            registerConfigurationScreenInteractor.validarPasswordTecnico(context, passAdmin);
        }
    }

    /**
     * Registra los parametros de conexion del dispositivo
     *
     * @param context
     * @param configurations
     */
    @Override
    public void registrarConfiguracionConexion(Context context, Configurations configurations) {
        registerConfigurationScreenInteractor.registrarConfiguracionConexion(context, configurations);
    }

    /**
     * Sobrecarga del metodo onEventMainThread de la interface SaldoScreenPresenter para el manejo de eventos
     *
     * @param registerConfigurationScreenEvent
     */
    @Override
    public void onEventMainThread(RegisterConfigurationScreenEvent registerConfigurationScreenEvent) {
        switch (registerConfigurationScreenEvent.getEventType()) {

            case RegisterConfigurationScreenEvent.onValorAccesoValido:
                onPasswordTecnicoValido();
                break;
            case RegisterConfigurationScreenEvent.onValorAccesoNoValido:
                onPasswordTecnicoNoValido();
                break;
            case RegisterConfigurationScreenEvent.onValorAccesoError:
                onPasswordTecnicoError();
                break;

            case RegisterConfigurationScreenEvent.onRegistroConfigConexionSuccess:
                onRegistroConfigConexionSuccess();
                break;
            case RegisterConfigurationScreenEvent.onRegistroConfigConexionError:
                onRegistroConfigConexionError();
                break;

            case RegisterConfigurationScreenEvent.onInformacionDispositivoSuccess:
                onInformacionDispositivoSuccess();
                break;
            case RegisterConfigurationScreenEvent.onInformacionDispositivoErrorConexion:
                onInformacionDispositivoErrorConexion();
                break;
            case RegisterConfigurationScreenEvent.onInformacionDispositivoErrorInformacion:
                onInformacionDispositivoErrorInformacion();
                break;

            case RegisterConfigurationScreenEvent.onProccessInformacionEstablecimientoSuccess:
                onProccessInformacionEstablecimientoSuccess();
                break;
            case RegisterConfigurationScreenEvent.onProccessInformacionEstablecimientoError:
                onProccessInformacionEstablecimientoError();
                break;
        }
    }


    /**
     * #############################################################################################
     * Metodo propios de la clase
     * #############################################################################################
     */

    /**
     * Metodo para manejar el password tecnico valido
     */
    private void onPasswordTecnicoValido() {
        if (registerConfigurationScreenView != null) {
            registerConfigurationScreenView.handlePasswordTecnicoValido();
        }
    }

    /**
     * Metodo para manejar el password tecnico NO valido
     */
    private void onPasswordTecnicoNoValido() {
        if (registerConfigurationScreenView != null) {
            registerConfigurationScreenView.handlePasswordTecnicoNoValido();
        }
    }

    /**
     * Metodo para manejar el error en el password tecnico
     */
    private void onPasswordTecnicoError() {
        if (registerConfigurationScreenView != null) {
            registerConfigurationScreenView.handlePasswordTecnicoError();
        }
    }

    /**
     * Metodo para manejar el registro de la configuracion exitoso
     */
    private void onRegistroConfigConexionSuccess() {
        if (registerConfigurationScreenView != null) {
            registerConfigurationScreenView.handleRegistroConfigConexionSuccess();
        }
    }

    /**
     * Metodo para manejar el registro de la configuracion erroneo
     */
    private void onRegistroConfigConexionError() {
        if (registerConfigurationScreenView != null) {
            registerConfigurationScreenView.handleRegistroConfigConexionError();
        }
    }

    /**
     * Metodo para manejar la consulta de la informacion del dispositivo exitosa
     */
    private void onInformacionDispositivoSuccess() {
        if (registerConfigurationScreenView != null) {
            registerConfigurationScreenView.handleInformacionDispositivoSuccess();
        }
    }

    /**
     * Metodo para manejar la consulta de la informacion del dispositivo erronea
     */
    private void onInformacionDispositivoErrorConexion() {
        if (registerConfigurationScreenView != null) {
            registerConfigurationScreenView.handleInformacionDispositivoErrorConexion();
        }
    }

    /**
     * Metodo para manejar la consulta de la informacion del dispositivo erronea
     */
    private void onInformacionDispositivoErrorInformacion() {
        if (registerConfigurationScreenView != null) {
            registerConfigurationScreenView.handleInformacionDispositivoErrorInformacion();
        }
    }

    /**
     * Metodo para manejar el registro de la informacion del dispositivo exitoso
     */
    private void onProccessInformacionEstablecimientoSuccess() {
        if (registerConfigurationScreenView != null) {
            registerConfigurationScreenView.handleProccessInformacionEstablecimientoSuccess();
        }
    }

    /**
     * Metodo para manejar el registro de la informacion del dispositivo erronea
     */
    private void onProccessInformacionEstablecimientoError() {
        if (registerConfigurationScreenView != null) {
            registerConfigurationScreenView.handleProccessInformacionEstablecimientoError();
        }
    }

}
