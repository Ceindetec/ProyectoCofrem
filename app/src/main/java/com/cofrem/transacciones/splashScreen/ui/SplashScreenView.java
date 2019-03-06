package com.cofrem.transacciones.splashScreen.ui;

/**
 * Interface usada en la actividad de la Pantalla principal
 */
public interface SplashScreenView {

    /**
     * Metodo para manejar la existencia de la configuracion inicial
     */
    void handleVerifyInitialConfigExiste();

    /**
     * Metodo para manejar la NO existencia de la configuracion inicial
     */
    void handleVerifyInitialConfigNoExiste();

    /**
     * Metodo para manejar la existencia de la configuracion inicial NO valida
     */
    void handleVerifyInitialConfigNoValida();

    /**
     * Metodo para manejar la existencia de la configuracion de acceso
     */
    void handleRegistroConfiguracionAccesoExiste();

    /**
     * Metodo para manejar la no existencia de la configuracion de acceso
     */
    void handleRegistroConfiguracionAccesoNoExiste();

    /**
     * Metodo para manejar el registro de la configuracion de acceso exitosa
     */
    void handleInsertRegistroConfiguracionAccesoSuccess();

    /**
     * Metodo para manejar el registro de la configuracion de acceso erronea
     */
    void handleInsertRegistroConfiguracionAccesoError();

    /**
     * Metodo para manejar la verificacion exitosa
     */
    void handleVerifySuccess();

    /**
     * Metodo para manejar la verificacion erronea
     */
    void handleVerifyError();

    /**
     * Metodo para manejar la conexion a internet exitosa
     */
    void handleInternetConnectionSuccess();

    /**
     * Metodo para manejar la conexion a internet erronea
     */
    void handleInternetConnectionError();

    /**
     * Metodo para manejar la conexion al dispositivo lector de banda magnetica exitosa
     */
    void handleMagneticReaderDeviceSuccess();

    /**
     * Metodo para manejar la conexion al dispositivo lector de banda magnetica erronea
     */
    void handleMagneticReaderDeviceError();

    /**
     * Metodo para manejar la conexion al dispositivo NFC exitosa
     */
    void handleNFCDeviceSuccess();

    /**
     * Metodo para manejar la conexion al dispositivo NFC erronea
     */
    void handleNFCDeviceError();

    /**
     * Metodo para manejar la conexion al dispositivo de impresion exitosa
     */
    void handlePrinterDeviceSuccess();

    /**
     * Metodo para manejar la conexion al dispositivo de impresion erronea
     */
    void handlePrinterDeviceError();

    /**
     * Metodo para manejar la conexion al dispositivo de impresion exitosa
     */
    void handleDeviceSuccess();

    /**
     * Metodo para manejar la conexion al dispositivo de impresion erronea
     */
    void handleDeviceError();

    /**
     * Metodo para manejar la obtencion de la informacion del header exitosa
     */
    void handleGetInfoHeaderSuccess();

    /**
     * Metodo para manejar la obtencion de la informacion del header erronea
     */
    void handleGetInfoHeaderError();


}
