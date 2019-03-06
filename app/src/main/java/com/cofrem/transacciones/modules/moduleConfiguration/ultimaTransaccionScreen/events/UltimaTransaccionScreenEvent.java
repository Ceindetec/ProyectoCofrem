package com.cofrem.transacciones.modules.moduleConfiguration.ultimaTransaccionScreen.events;

public class UltimaTransaccionScreenEvent {

    public final static int onVerifySuccess = 0;
    public final static int onValidarUltimaTransaccionCorrecta = 1;
    public final static int onValidarUltimaTransaccionErronea = 2;
    public final static int onValidarUltimaTransaccionError = 3;
    public final static int onTransaccionWSConexionError = 4;

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
