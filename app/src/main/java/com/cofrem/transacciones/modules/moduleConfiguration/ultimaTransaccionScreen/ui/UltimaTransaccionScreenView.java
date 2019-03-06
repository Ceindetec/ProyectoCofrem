package com.cofrem.transacciones.modules.moduleConfiguration.ultimaTransaccionScreen.ui;

public interface UltimaTransaccionScreenView {

    /**
     * Metodo para manejar el test de comunicacion exitoso
     */
    void onValidarUltimaTransaccionCorrecta();

    /**
     * Metodo para manejar el test de comunicacion exitoso
     */
    void onValidarUltimaTransaccionErronea();

    /**
     * Metodo para manejar el test de comunicacion con error
     */
    void onValidarUltimaTransaccionError(String error);

    /**
     * Metodo para manejar la el error en la comunicacion con el WS
     */
    void handleTransaccionWSConexionError();

}
