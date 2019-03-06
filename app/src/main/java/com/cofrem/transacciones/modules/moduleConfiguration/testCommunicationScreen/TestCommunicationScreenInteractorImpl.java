package com.cofrem.transacciones.modules.moduleConfiguration.testCommunicationScreen;

import android.content.Context;

class TestCommunicationScreenInteractorImpl implements TestCommunicationScreenInteractor {
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
    private TestCommunicationScreenRepository testCommunicationScreenRepository;

    /**
     * #############################################################################################
     * Constructor de la clase
     * #############################################################################################
     */
    public TestCommunicationScreenInteractorImpl() {

        testCommunicationScreenRepository = new TestCommunicationScreenRepositoryImpl();

    }

    /**
     * #############################################################################################
     * Metodos sobrecargados de la interface
     * #############################################################################################
     */


    @Override
    public void testComunication(Context context) {
        testCommunicationScreenRepository.testComunication(context);
    }

    /**
     *
     */
    @Override
    public void validateAccess() {
        //Valida el acceso a la app
        testCommunicationScreenRepository.validateAcces();
    }
}
