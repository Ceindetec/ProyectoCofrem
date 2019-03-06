package com.cofrem.transacciones.splashScreen;

import android.content.Context;

public interface SplashScreenInteractor {

    /**
     * Metodo que verifica:
     * - La existencia de la configuración inicial
     * - En caso de no existir mostrará la vista de configuración
     * - En caso de existir validara el acceso
     *
     * @param context
     */
    void validateInitialConfig(Context context);

    /**
     * Metodo que verifica:
     * - Conexion a internet
     * - Existencia datos en DB interna
     * - Coherencia de datos con el servidor
     *
     * @param context
     */
    void validateAccess(Context context);

    /**
     * Metodo que consulta la informacion del header
     *
     * @param context
     */
    void setInfoHeader(Context context);

}
