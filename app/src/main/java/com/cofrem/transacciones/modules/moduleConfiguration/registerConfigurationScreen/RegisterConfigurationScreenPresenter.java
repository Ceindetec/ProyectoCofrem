package com.cofrem.transacciones.modules.moduleConfiguration.registerConfigurationScreen;

import android.content.Context;

import com.cofrem.transacciones.models.Configurations;
import com.cofrem.transacciones.modules.moduleConfiguration.registerConfigurationScreen.events.RegisterConfigurationScreenEvent;

public interface RegisterConfigurationScreenPresenter {

    /**
     * Valida el acceso a la configuracion del dispositivo mediante la contraseña de administrador
     *
     * @param context
     * @param passAdmin
     */
    void validarPasswordTecnico(Context context, String passAdmin);

    /**
     * Registra los parametros de conexion del dispositivo
     *
     * @param context
     * @param configurations
     */
    void registrarConfiguracionConexion(Context context, Configurations configurations);

    /**
     * Metodo para la creacion del presentador
     */
    void onCreate();

    /**
     * Metodo para la destruccion del presentador
     */
    void onDestroy();


    /**
     * Metodo para recibir los eventos generados
     *
     * @param splashScreenEvent
     */
    void onEventMainThread(RegisterConfigurationScreenEvent splashScreenEvent);

}
