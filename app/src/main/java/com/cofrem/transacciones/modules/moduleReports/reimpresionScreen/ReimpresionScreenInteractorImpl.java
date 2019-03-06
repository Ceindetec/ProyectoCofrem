package com.cofrem.transacciones.modules.moduleReports.reimpresionScreen;

import android.content.Context;

class ReimpresionScreenInteractorImpl implements ReimpresionScreenInteractor {
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
    private ReimpresionScreenRepository reimpresionScreenRepository;

    /**
     * #############################################################################################
     * Constructor de la clase
     * #############################################################################################
     */
    public ReimpresionScreenInteractorImpl() {

        reimpresionScreenRepository = new ReimpresionScreenRepositoryImpl();

    }

    @Override
    public void validarClaveAdministrador(Context context, String clave) {
        reimpresionScreenRepository.validarClaveAdministrador(context, clave);
    }

    @Override
    public void validarExistenciaUltimoRecibo(Context context) {
        reimpresionScreenRepository.validarExistenciaUltimoRecibo(context);
    }

    @Override
    public void imprimirUltimoRecibo(Context context) {
        reimpresionScreenRepository.imprimirUltimoRecibo(context);
    }

    @Override
    public void validarExistenciaReciboConNumCargo(Context context, String numCargo) {
        reimpresionScreenRepository.validarExistenciaReciboConNumCargo(context, numCargo);
    }

    @Override
    public void imprimirReciboConNumCargo(Context context) {
        reimpresionScreenRepository.imprimirReciboConNumCargo(context);
    }

    @Override
    public void validarExistenciaDetalleRecibos(Context context) {
        reimpresionScreenRepository.validarExistenciaDetalleRecibos(context);
    }

    @Override
    public void imprimirReporteDetalle(Context context) {
        reimpresionScreenRepository.imprimirReporteDetalle(context);
    }

    @Override
    public void imprimirReporteGeneral(Context context) {
        reimpresionScreenRepository.imprimirReporteGeneral(context);
    }

    @Override
    public void cierreDeLote(Context context) {
        reimpresionScreenRepository.cierreDeLote(context);
    }

    @Override
    public void imprimirCierreLote(Context context) {
        reimpresionScreenRepository.imprimirCierreLote(context);
    }


    /**
     * #############################################################################################
     * Metodos sobrecargados de la interface
     * #############################################################################################
     */


}
