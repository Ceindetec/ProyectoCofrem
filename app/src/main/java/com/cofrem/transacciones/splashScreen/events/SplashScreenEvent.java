package com.cofrem.transacciones.splashScreen.events;

/**
 * Clase que maneja los eventos de la Pantalla de Inicio
 */
public class SplashScreenEvent {

    /**
     * Eventos asociados a la Pantalla de Inicio
     */
    // Eventos de verificacion de disponibilidad del sistema
    public final static int onVerifyInitialConfigExiste = 0;
    public final static int onVerifyInitialConfigNoExiste = 1;
    public final static int onVerifyInitialConfigNoValida = 2;

    public final static int onRegistroConfiguracionAccesoExiste = 3;
    public final static int onRegistroConfiguracionAccesoNoExiste = 4;

    public final static int onInsertRegistroConfiguracionAccesoSuccess = 5;
    public final static int onInsertRegistroConfiguracionAccesoError = 6;

    public final static int onVerifySuccess = 7;
    public final static int onVerifyError = 8;

    public final static int onInternetConnectionSuccess = 9;
    public final static int onInternetConnectionError = 10;

    public final static int onMagneticReaderDeviceSuccess = 11;
    public final static int onMagneticReaderDeviceError = 12;

    public final static int onNFCDeviceSuccess = 13;
    public final static int onNFCDeviceError = 14;

    public final static int onPrinterDeviceSuccess = 15;
    public final static int onPrinterDeviceError = 16;

    public final static int onDeviceSuccess = 17;
    public final static int onDeviceError = 18;

    public final static int onGetInfoHeaderSuccess = 19;
    public final static int onGetInfoHeaderError = 20;


    // Variable que maneja los tipos de eventos
    private int eventType;

    // Variable que maneja los mensajes de error de los eventos
    private String errorMessage;

    //Getters y Setters de la clase

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
