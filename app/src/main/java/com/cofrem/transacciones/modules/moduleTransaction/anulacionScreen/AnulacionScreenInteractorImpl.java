package com.cofrem.transacciones.modules.moduleTransaction.anulacionScreen;

import android.content.Context;

import com.cofrem.transacciones.models.Transaccion;

class AnulacionScreenInteractorImpl implements AnulacionScreenInteractor {
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
    private AnulacionScreenRepository anulacionScreenRepository;

    /**
     * #############################################################################################
     * Constructor de la clase
     * #############################################################################################
     */
    public AnulacionScreenInteractorImpl() {

        anulacionScreenRepository = new AnulacionScreenRepositoryImpl();

    }
    /**
     * #############################################################################################
     * Metodos sobrecargados de la interface
     * #############################################################################################
     */

    /**
     * @param context
     * @param passAdmin
     */
    @Override
    public void validarPasswordAdministrador(Context context, String passAdmin) {
        //Valida el acceso a la app mediante la clave de administracion
        anulacionScreenRepository.validarPasswordAdministrador(context, passAdmin);
    }

    /**
     * @param context
     * @param numeroCargo
     */
    @Override
    public void obtenerValorTransaccion(Context context, String numeroCargo) {
        //Obtiene el valor de la transaccion identificada con el numero de cargo
        anulacionScreenRepository.obtenerValorTransaccion(context, numeroCargo);
    }

    /**
     * @param context
     * @param transaccion
     */
    @Override
    public void registrarTransaccion(Context context, Transaccion transaccion) {
        //Registra la transaccion de anulacion
        anulacionScreenRepository.registrarTransaccion(context, transaccion);
    }

    @Override
    public void imprimirRecibo(Context context, String stringCopia) {
        anulacionScreenRepository.imprimirRecibo(context, stringCopia);
    }
}
