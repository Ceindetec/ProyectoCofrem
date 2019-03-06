package com.cofrem.transacciones.modules.moduleConfiguration.ultimaTransaccionScreen;

import android.content.Context;

class UltimaTransaccionScreenInteractorImpl implements UltimaTransaccionScreenInteractor {
    /**
     * #############################################################################################
     * Declaracion de componentes y variables
     * #############################################################################################
     */

    /**
     * #############################################################################################
     * Instanciamientos de las clases
     * #############################################################################################
     */
    private UltimaTransaccionScreenRepository ultimaTransaccionScreenRepository;

    /**
     * #############################################################################################
     * Constructor de la clase
     * #############################################################################################
     */
    public UltimaTransaccionScreenInteractorImpl() {

        ultimaTransaccionScreenRepository = new UltimaTransaccionScreenRepositoryImpl();

    }

    /**
     * #############################################################################################
     * Metodos sobrecargados de la interface
     * #############################################################################################
     */

    /**
     * @param context
     */
    @Override
    public void validarUltimaTransaccion(Context context) {
        ultimaTransaccionScreenRepository.validarUltimaTransaccion(context);
    }

}
