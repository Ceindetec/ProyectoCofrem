package com.cofrem.transacciones.modules.moduleConfiguration.registerConfigurationScreen;

import android.content.Context;

import com.cofrem.transacciones.models.Configurations;

public interface RegisterConfigurationScreenRepository {
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
}
