package com.cofrem.transacciones.Modules.ModuleConfiguration.ConfigurationPrinter.ui;

/**
 * Interface usada en la actividad de la Pantalla principal
 */
public interface ConfigurationPrinterScreenView {

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


}
