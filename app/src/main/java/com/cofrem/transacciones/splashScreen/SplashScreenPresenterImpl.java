package com.cofrem.transacciones.splashScreen;

import android.content.Context;

import com.cofrem.transacciones.lib.EventBus;
import com.cofrem.transacciones.lib.GreenRobotEventBus;
import com.cofrem.transacciones.splashScreen.events.SplashScreenEvent;
import com.cofrem.transacciones.splashScreen.ui.SplashScreenView;

public class SplashScreenPresenterImpl implements SplashScreenPresenter {

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
    //Instanciamiento de la interface SplashScreenView
    private SplashScreenView splashScreenView;

    //Instanciamiento de la interface SplashScreenInteractor
    private SplashScreenInteractor splashScreenInteractor;

    /**
     * #############################################################################################
     * Constructor de la clase
     * #############################################################################################
     *
     * @param splashScreenView
     */
    public SplashScreenPresenterImpl(SplashScreenView splashScreenView) {
        this.splashScreenView = splashScreenView;
        this.splashScreenInteractor = new SplashScreenInteractorImpl();
        this.eventBus = GreenRobotEventBus.getInstance();
    }

    /**
     * #############################################################################################
     * Metodos sobrecargados de la interface
     * #############################################################################################
     */

    /**
     * Sobrecarga del metodo onCreate de la interface SplashScreenPresenter "crear" el registro al bus de eventos
     */
    @Override
    public void onCreate() {

        eventBus.register(this);

    }

    /**
     * Sobrecarga del metodo onDestroy de la interface SplashScreenPresenter para "eliminar"  el registro al bus de eventos
     */
    @Override
    public void onDestroy() {
        splashScreenView = null;
        eventBus.unregister(this);
    }

    /**
     * Metodo que verifica:
     * - La existencia de la configuración inicial
     * - En caso de no existir mostrará la vista de configuración
     * - En caso de existir validara el acceso
     *
     * @param context
     */
    @Override
    public void validateInitialConfig(Context context) {
        if (splashScreenView != null) {
            splashScreenInteractor.validateInitialConfig(context);
        }
    }

    /**
     * Metodo que consulta la informacion del header
     *
     * @param context
     */
    @Override
    public void setInfoHeader(Context context) {
        if (splashScreenView != null) {
            splashScreenInteractor.setInfoHeader(context);
        }
    }

    /**
     * Metodo que verifica:
     * - Conexion a internet
     * - Existencia datos en DB interna
     * - Coherencia de datos con el servidor
     *
     * @param context
     */
    @Override
    public void validateAccess(Context context) {
        if (splashScreenView != null) {
            splashScreenInteractor.validateAccess(context);
        }
    }

    /**
     * Sobrecarga del metodo onEventMainThread de la interface SplashScreenPresenter para el manejo de eventos
     *
     * @param splashScreenEvent
     */
    @Override
    public void onEventMainThread(SplashScreenEvent splashScreenEvent) {
        switch (splashScreenEvent.getEventType()) {

            case SplashScreenEvent.onVerifyInitialConfigExiste:
                onVerifyInitialConfigExiste();
                break;

            case SplashScreenEvent.onVerifyInitialConfigNoExiste:
                onVerifyInitialConfigNoExiste();
                break;

            case SplashScreenEvent.onVerifyInitialConfigNoValida:
                onVerifyInitialConfigNoValida();
                break;

            case SplashScreenEvent.onRegistroConfiguracionAccesoExiste:
                onRegistroConfiguracionAccesoExiste();
                break;

            case SplashScreenEvent.onRegistroConfiguracionAccesoNoExiste:
                onRegistroConfiguracionAccesoNoExiste();
                break;

            case SplashScreenEvent.onInsertRegistroConfiguracionAccesoSuccess:
                onInsertRegistroConfiguracionAccesoSuccess();
                break;

            case SplashScreenEvent.onInsertRegistroConfiguracionAccesoError:
                onInsertRegistroConfiguracionAccesoError();
                break;

            case SplashScreenEvent.onVerifySuccess:
                onVerifySuccess();
                break;

            case SplashScreenEvent.onVerifyError:
                onVerifyError();
                break;

            case SplashScreenEvent.onInternetConnectionSuccess:
                onInternetConnectionSuccess();
                break;

            case SplashScreenEvent.onInternetConnectionError:
                onInternetConnectionError();
                break;

            case SplashScreenEvent.onMagneticReaderDeviceSuccess:
                onMagneticReaderDeviceSuccess();
                break;

            case SplashScreenEvent.onMagneticReaderDeviceError:
                onMagneticReaderDeviceError();
                break;

            case SplashScreenEvent.onNFCDeviceSuccess:
                onNFCDeviceSuccess();
                break;

            case SplashScreenEvent.onNFCDeviceError:
                onNFCDeviceError();
                break;

            case SplashScreenEvent.onPrinterDeviceSuccess:
                onPrinterDeviceSuccess();
                break;

            case SplashScreenEvent.onPrinterDeviceError:
                onPrinterDeviceError();
                break;

            case SplashScreenEvent.onDeviceSuccess:
                onDeviceSuccess();
                break;

            case SplashScreenEvent.onDeviceError:
                onDeviceError();
                break;

            case SplashScreenEvent.onGetInfoHeaderSuccess:
                onGetInfoHeaderSuccess();
                break;

            case SplashScreenEvent.onGetInfoHeaderError:
                onGetInfoHeaderError();
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
    private void onVerifyInitialConfigExiste() {
        if (splashScreenView != null) {
            splashScreenView.handleVerifyInitialConfigExiste();
        }
    }

    /**
     * Metodo para manejar la verificacion exitosa
     */
    private void onVerifyInitialConfigNoExiste() {
        if (splashScreenView != null) {
            splashScreenView.handleVerifyInitialConfigNoExiste();
        }
    }

    /**
     * Metodo para manejar la existencia de la configuracion inicial NO valida
     */
    private void onVerifyInitialConfigNoValida() {
        if (splashScreenView != null) {
            splashScreenView.handleVerifyInitialConfigNoValida();
        }
    }

    /**
     * Metodo para manejar la existencia de la configuracion de acceso
     */
    private void onRegistroConfiguracionAccesoExiste() {
        if (splashScreenView != null) {
            splashScreenView.handleRegistroConfiguracionAccesoExiste();
        }
    }

    /**
     * Metodo para manejar la no existencia de la configuracion de acceso
     */
    private void onRegistroConfiguracionAccesoNoExiste() {
        if (splashScreenView != null) {
            splashScreenView.handleRegistroConfiguracionAccesoNoExiste();
        }
    }

    /**
     * Metodo para manejar el registro de la configuracion de acceso exitosa
     */
    private void onInsertRegistroConfiguracionAccesoSuccess() {
        if (splashScreenView != null) {
            splashScreenView.handleInsertRegistroConfiguracionAccesoSuccess();
        }
    }

    /**
     * Metodo para manejar el registro de la configuracion de acceso erronea
     */
    private void onInsertRegistroConfiguracionAccesoError() {
        if (splashScreenView != null) {
            splashScreenView.handleInsertRegistroConfiguracionAccesoError();
        }
    }

    /**
     * Metodo para manejar la verificacion exitosa
     */
    private void onVerifySuccess() {
        if (splashScreenView != null) {
            splashScreenView.handleVerifySuccess();
        }
    }

    /**
     * Metodo para manejar la verificacion erronea
     */
    private void onVerifyError() {
        if (splashScreenView != null) {
            splashScreenView.handleVerifyError();
        }
    }

    /**
     * Metodo para manejar la conexion a internet exitosa
     */
    private void onInternetConnectionSuccess() {
        if (splashScreenView != null) {
            splashScreenView.handleInternetConnectionSuccess();
        }
    }

    /**
     * Metodo para manejar la conexion a internet erronea
     */
    private void onInternetConnectionError() {
        if (splashScreenView != null) {
            splashScreenView.handleInternetConnectionError();
        }
    }

    /**
     * Metodo para manejar la conexion al dispositivo lector de banda magnetica exitosa
     */
    private void onMagneticReaderDeviceSuccess() {
        if (splashScreenView != null) {
            splashScreenView.handleMagneticReaderDeviceSuccess();
        }
    }

    /**
     * Metodo para manejar la conexion al dispositivo lector de banda magnetica erronea
     */
    private void onMagneticReaderDeviceError() {
        if (splashScreenView != null) {
            splashScreenView.handleMagneticReaderDeviceError();
        }
    }

    /**
     * Metodo para manejar la conexion al dispositivo NFC exitosa
     */
    private void onNFCDeviceSuccess() {
        if (splashScreenView != null) {
            splashScreenView.handleNFCDeviceSuccess();
        }
    }

    /**
     * Metodo para manejar la conexion al dispositivo NFC erronea
     */
    private void onNFCDeviceError() {
        if (splashScreenView != null) {
            splashScreenView.handleNFCDeviceError();
        }
    }

    /**
     * Metodo para manejar la conexion al dispositivo de impresion exitosa
     */
    private void onPrinterDeviceSuccess() {
        if (splashScreenView != null) {
            splashScreenView.handlePrinterDeviceSuccess();
        }
    }

    /**
     * Metodo para manejar la conexion al dispositivo de impresion erronea
     */
    private void onPrinterDeviceError() {
        if (splashScreenView != null) {
            splashScreenView.handlePrinterDeviceError();
        }
    }

    /**
     * Metodo para manejar la conexion al dispositivo de impresion exitosa
     */
    private void onDeviceSuccess() {
        if (splashScreenView != null) {
            splashScreenView.handleDeviceSuccess();
        }
    }

    /**
     * Metodo para manejar la conexion al dispositivo de impresion erronea
     */
    private void onDeviceError() {
        if (splashScreenView != null) {
            splashScreenView.handleDeviceError();
        }
    }

    /**
     * Metodo para manejar la obtencion de la informacion del header exitosa
     */
    private void onGetInfoHeaderSuccess() {
        if (splashScreenView != null) {
            splashScreenView.handleGetInfoHeaderSuccess();
        }
    }

    /**
     * Metodo para manejar la obtencion de la informacion del header erronea
     */
    private void onGetInfoHeaderError() {
        if (splashScreenView != null) {
            splashScreenView.handleGetInfoHeaderError();
        }
    }
}
