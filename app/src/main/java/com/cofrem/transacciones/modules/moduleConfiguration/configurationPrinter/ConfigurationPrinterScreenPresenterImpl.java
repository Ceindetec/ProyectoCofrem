package com.cofrem.transacciones.modules.moduleConfiguration.configurationPrinter;


import android.content.Context;

import com.cofrem.transacciones.lib.EventBus;
import com.cofrem.transacciones.lib.GreenRobotEventBus;
import com.cofrem.transacciones.models.ConfigurationPrinter;
import com.cofrem.transacciones.modules.moduleConfiguration.configurationPrinter.events.ConfigurationPrinterScreenEvent;
import com.cofrem.transacciones.modules.moduleConfiguration.configurationPrinter.ui.ConfigurationPrinterScreenView;

public class ConfigurationPrinterScreenPresenterImpl implements ConfigurationPrinterScreenPresenter {


    /**
     * #############################################################################################
     * Declaracion de componentes y variables
     * #############################################################################################
     */
    //Declaracion del bus de eventos
    EventBus eventBus;

    /**
     * #############################################################################################
     * Instanciamientos de las clases
     * #############################################################################################
     */
    //Instanciamiento de la interface configurationPrinterScreenView
    private ConfigurationPrinterScreenView configurationPrinterScreenView;

    //Instanciamiento de la interface ConfigurationPrinterScreenInteractor
    private ConfigurationPrinterScreenInteractor configurationPrinterScreenInteractor;

    /**
     * #############################################################################################
     * Constructor de la clase
     * #############################################################################################
     *
     * @param configurationPrinterScreenView
     */
    public ConfigurationPrinterScreenPresenterImpl(ConfigurationPrinterScreenView configurationPrinterScreenView) {
        this.configurationPrinterScreenView = configurationPrinterScreenView;
        this.configurationPrinterScreenInteractor = new ConfigurationPrinterScreenInteractorImpl();
        this.eventBus = GreenRobotEventBus.getInstance();
    }

    /**
     * Sobrecarga del metodo onCreate de la interface SaldoScreenPresenter "crear" el registro al bus de eventos
     */
    @Override
    public void onCreate() {

        eventBus.register(this);

    }

    /**
     * Sobrecarga del metodo onDestroy de la interface SaldoScreenPresenter para "eliminar"  el registro al bus de eventos
     */
    @Override
    public void onDestroy() {
        configurationPrinterScreenView = null;
        eventBus.unregister(this);
    }

    /**
     * metodo que se encarga de verificar la existencia de la configuracion de la impresora
     */
    @Override
    public void VerifyConfigurationInitialPrinter(Context context) {
        if (configurationPrinterScreenView != null) {
            configurationPrinterScreenInteractor.VerifyConfigurationInitialPrinter(context);
        }
    }

    /**
     * metodo que se encarga guardar la configuracion de la impresora
     */
    @Override
    public void saveConfigurationPrinter(Context context, ConfigurationPrinter configuration) {
        if (configurationPrinterScreenView != null) {
            configurationPrinterScreenInteractor.saveConfigurationPrinter(context, configuration);
        }
    }

    @Override
    public void imprimirPrueba(Context context, int gray) {
        if (configurationPrinterScreenView != null) {
            configurationPrinterScreenInteractor.imprimirPrueba(context, gray);
        }
    }

    /**
     * Sobrecarga del metodo onEventMainThread de la interface SaldoScreenPresenter para el manejo de eventos
     *
     * @param configurationPrinterScreenEvent
     */
    @Override
    public void onEventMainThread(ConfigurationPrinterScreenEvent configurationPrinterScreenEvent) {
        switch (configurationPrinterScreenEvent.getEventType()) {

            case ConfigurationPrinterScreenEvent.onVerifyConfigurationInitialSuccess:
                onVerifyConfigurationInitialPrinterSuccess(configurationPrinterScreenEvent.getConfigurationPrinter());
                break;
            case ConfigurationPrinterScreenEvent.onVerifyConfigurationInitialError:
                break;
            case ConfigurationPrinterScreenEvent.onSaveConfigurationPrinterSuccess:
                onSaveConfigurationPrinterSuccess();
                break;
            case ConfigurationPrinterScreenEvent.onSaveConfigurationPrinterError:
                onSaveConfigurationPrinterError();
                break;
            case ConfigurationPrinterScreenEvent.onPrintTestSuccess:
                onPrintTestSuccess();
                break;
            case ConfigurationPrinterScreenEvent.onPrintTestError:
                onPrintTestError(configurationPrinterScreenEvent.getErrorMessage());
                break;

        }
    }

/**
 * #############################################################################################
 * Metodo propios de la clase
 * #############################################################################################
 */

    /**
     * Metodo para manejar la verificacion exitosa
     */
    private void onVerifyConfigurationInitialPrinterSuccess(ConfigurationPrinter configuration) {
        if (configurationPrinterScreenView != null) {
            configurationPrinterScreenView.handleVerifyConfigurationInitialPrinterSuccess(configuration);
        }
    }


    /**
     * Metodo para manejar la verificacion Errada
     */
    private void onVerifyConfigurationInitialPrinterError() {
        if (configurationPrinterScreenView != null) {
        }
    }

    /**
     * Metodo para manejar el insert de la configuracion exitosa
     */
    private void onSaveConfigurationPrinterSuccess() {
        if (configurationPrinterScreenView != null) {
            configurationPrinterScreenView.handleSaveConfigurationPrinterSuccess();
        }
    }

    /**
     * Metodo para manejar el insert de la configuracion con error
     */
    private void onSaveConfigurationPrinterError() {
        if (configurationPrinterScreenView != null) {
            configurationPrinterScreenView.handleSaveConfigurationPrinterError();
        }
    }

    /**
     * Metodo para manejar la impression de prueba exitosa
     */
    private void onPrintTestSuccess() {
        if (configurationPrinterScreenView != null) {
            configurationPrinterScreenView.handlePrintTestSuccess();
        }
    }

    /**
     * Metodo para manejar la impression de prueba con error
     */
    private void onPrintTestError(String messageError) {
        if (configurationPrinterScreenView != null) {
            configurationPrinterScreenView.handlePrintTestError(messageError);
        }
    }


}
